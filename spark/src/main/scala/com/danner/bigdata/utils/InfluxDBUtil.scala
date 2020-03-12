package com.danner.bigdata.utils

object InfluxDBUtil {

    def getInfluxIP:String ={
        var ip = "hadoop002"

        val map = System.getenv()
        if(map.containsKey("INFLUXDB_IP")){
            ip = map.get("INFLUXDB_IP")
        }
        ip
    }

    def getRandomMeasurement:String = "measurement_" + System.nanoTime()

    def getInfluxPORT(apiPort:Boolean):String = {
        var port = "8086"

        val map = System.getenv()
        if(apiPort){
            if (map.containsKey("INFLUXDB_PORT_API")){
                port = map.get("INFLUXDB_PORT_API")
            }
        }else{
            port = "8096"
            if (map.containsKey("INFLUXDB_PORT_COLLECTD")){
                port = map.get("INFLUXDB_PORT_COLLECTD")
            }
        }
        port
    }

    def defaultRetentionPolicy(version:String):String = {
        if (version.startsWith("0."))
            "default"
        else
            "autogen"
    }
}
