package me.codechallenge

import grails.gorm.transactions.Transactional
import grails.rest.RestfulController
import grails.web.http.HttpHeaders

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.CREATED

class EventController extends RestfulController<Event>{

    def messageSource

    static final SimpleDateFormat dateFormat = new SimpleDateFormat('yyyy-MM-dd')

    static responseFormats = ['json', 'xml']

    EventController() {
        super(Event, false)
    }

    @Transactional
    def save() {
        if(handleReadOnly()) {
            return
        }

        def jsonParams = request.JSON

        def instance = new Event()
        instance.name = jsonParams["name"]
        instance.description = jsonParams["description"]

        if (!jsonParams["name"]) {
            respond([message: messageSource.getMessage('event.name.missing', null, "Event name is required", Locale.ENGLISH)])
            return
        }

        if (jsonParams["startTime"]) {
            def startTime = validateAndReturnTime(jsonParams["startTime"])
            if (startTime > -1) {
                instance.startTime = startTime
            } else {
                return
            }

        }

        if (jsonParams["endTime"]) {
            def endTime = validateAndReturnTime(jsonParams["endTime"])
            if (endTime > -1) {
                instance.endTime = endTime
            } else {
                return
            }

        }

        if (jsonParams["startDate"]) {
            def startDate = validateAndReturnDate(jsonParams["startDate"])
            if (startDate) {
                instance.startDate = startDate
            } else {
                return
            }
        }

        if (jsonParams["endDate"]) {
            def endDate = validateAndReturnDate(jsonParams["endDate"])
            if (endDate) {
                instance.endDate = endDate
            } else {
                return
            }
        }

        if (jsonParams["recurring"]) {
            instance.recurring = Boolean.parseBoolean(jsonParams["recurring"])
        }

        if (jsonParams["fullDayEvent"]) {
            instance.fullDayEvent = Boolean.parseBoolean(jsonParams["fullDayEvent"])
        }

        if (jsonParams["timezone"]) { //this makes sure we save UTC if the time zone by user is incorrect
            def t = TimeZone.getTimeZone(jsonParams["timezone"])
            instance.timezone = t.getID()
        }

        //@TODO: validations for weekOfDay, dayOfMonth, etc. will be added here

        instance.validate()
        if (instance.hasErrors()) {
            respond instance.errors
            return
        }

        instance.save(flush:true, failOnError: true)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [classMessageArg, instance.id])
                redirect instance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond instance, [status: CREATED, view:'show']
            }
        }
    }

    private int validateAndReturnTime(def time) {
        def res = -1
        try {
            Integer.parseInt(time)

            def hour = time.substring(0, 2)
            def mins = time.substring(2)
            res = (Integer.parseInt(hour) * 60) + Integer.parseInt(mins)

        } catch(NumberFormatException){
            respond([message: messageSource.getMessage('event.time.invalid', null, "Time must be in hhmm format (ex: 2344)", Locale.ENGLISH)])
        }

       res
    }

    private validateAndReturnDate(def date) {
        def stDate = null
        try {
             dateFormat.setLenient(false)
             stDate = dateFormat.parse(date)
        }
        catch (ParseException) {
            respond([message: messageSource.getMessage('date.format.invalid', null, "Date must be in yyyy-MM-dd format: ex: 2021-09-23", Locale.ENGLISH)])
        }
        stDate
    }

}
