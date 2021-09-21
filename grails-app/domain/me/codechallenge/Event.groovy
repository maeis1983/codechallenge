package me.codechallenge

import org.grails.datastore.gorm.GormEntity

class Event implements GormEntity<Event> {

    String name
    String description

    Date startDate
    Date endDate
    int startTime // number of minutes since midnight: 70 = 01:10, 152 = 02:32
    int endTime  // same as startTime

    String timezone //Saved in Java's TimeZone IDs ex: Europe/Berlin

    boolean fullDayEvent = false
    boolean recurring = false

    int dayOfWeek = -1  //1 for Monday, 7 for Sunday
    int dayOfMonth = -1
    int weekOfMonth = -1 //1 for First week, 2 for second etc
    int monthOfYear = -1

    Date dateCreated
    Date lastUpdated

    static constraints = {
        description nullable: true
        startDate nullable: true
        endDate nullable: true
        startTime nullable: true
        endTime nullable: true
        timezone nullable: true
    }


}
