package app.volMP.u.rel

data class Event(val idevents: Int, var ename: String,
                 val eplace: String, val edate: String,
                 val etime: String, val etimezone: String, val einfo: String,
                 val epeople:String, val epoints:String, val eprivate:Int, val eispublished:Boolean,
                 val eisarhived:Boolean)