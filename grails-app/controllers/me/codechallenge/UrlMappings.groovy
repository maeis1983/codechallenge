package me.codechallenge

class UrlMappings {

    static String version = "v1"

    static mappings = {
        get "/api/${version}/$controller(.$format)?"(action:"index")
        get "/api/${version}/$controller/$id(.$format)?"(action:"show")
        post "/api/${version}/$controller(.$format)?"(action:"save")

        "/"(controller: 'event', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
