package me.codechallenge

import java.text.SimpleDateFormat

class BootStrap {

    def init = { servletContext ->

        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd")

        new Event(name: 'eventFixedDate', startDate: formatterDate.parse("2021-10-10"),
                endDate: formatterDate.parse("2021-10-10"),
                startTime: 9 * 60, timezone: 'Asia/Yerevan',
                endTime: (10 * 60) + 15).save()

        def oneYearLater = Calendar.getInstance()
        oneYearLater.add(Calendar.YEAR, 1)

        new Event(name: 'everyMondayWholeYear', startDate: new Date(), timezone: 'Europe/Berlin',
                recurring: true, dayOfWeek: 1, endDate: oneYearLater.getTime(), fullDayEvent: true).save()

        new Event(name: 'everyFirstSundayOfMonth', startDate: new Date(), timezone: 'Europe/Berlin',
                recurring: true, dayOfWeek: 7, weekOfMonth: 1, endDate: oneYearLater.getTime(), fullDayEvent: true).save()

    }
    def destroy = {
    }
}
