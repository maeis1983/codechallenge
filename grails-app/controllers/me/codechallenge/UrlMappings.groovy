package me.codechallenge

class UrlMappings {

    static mappings = {
        get "/api/v1/$controller(.$format)?"(action:"index")
        get "/api/v1/$controller/$id(.$format)?"(action:"show")
        post "/api/v1/$controller(.$format)?"(action:"save")

        "/"(controller: 'event', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
