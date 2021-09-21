package me.codechallenge

import grails.test.hibernate.HibernateSpec
import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import groovyx.net.http.RESTClient
import net.sf.json.JSONObject

@Integration
class EventControllerSpec extends HibernateSpec implements ControllerUnitTest<EventController> {

    void "testListEvents"() {

        when:
            controller.index()

        then:
            response.json.size() == 3 //created in Bootstrap by default
    }

    void "createEventMissingName"() {

        def result
        setup:
        controller.readOnly = false
        controller.request.contentType = "text/json"
        controller.request.json = [
                name: ""
        ]

        when:
        controller.save()
        result = controller.response

        then:
        result.json.message == 'Event name is required'
    }

    void "createEvent"() {

        def result
        setup:
            controller.readOnly = false
            controller.request.contentType = "text/json"
            controller.request.json = [
                    name: "event1"
            ]

        when:
            controller.save()
            result = controller.response

        then:
            result.json.name == 'event1'
    }

    void "createEvent with invalid date"() {

        def result
        setup:
            controller.readOnly = false
            controller.request.contentType = "text/json"
            controller.request.json = [
                    name: "event1",
                    startDate: '2022-222-2'
            ]

        when:
            controller.save()
            result = controller.response

        then:
            result.json.message.startsWith("Date must be in yyyy-MM-dd format")
    }

    void "createEventWithURL"() {

        def result
        setup:
        def endpoint = new RESTClient( 'http://localhost:8080/' )

        when:
            def input = new JSONObject()
            def randomName = "name" + String.valueOf(new Date().time)
            input.put("name", randomName)
            input.put("startDate", "2021-12-14")
            input.put("recurring", "true")
            input.put("startTime", "0823")
            input.put("endTime", "1211")

            result = endpoint.post([ path: '/api/v1/event', contentType : "application/json", body: input])

        then:
        result.responseData.name == randomName
    }

    void "get event by id"() {

        def result
        setup:
        def endpoint = new RESTClient( 'http://localhost:8080/' )

        when:
            result = endpoint.get([ path: '/api/v1/event/1', contentType : "application/json"])
        then:
           result.responseData.name  == Event.get(1).name
    }

}
