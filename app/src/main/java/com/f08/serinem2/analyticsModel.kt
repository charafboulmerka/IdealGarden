package com.f08.serinem2

class analyticsModel {
    var IP:String?=null
    var isManually:Boolean?=null
    var isNight:Boolean?=null
    var isWaterON:Boolean?=null
    var isDrySoil:Boolean?=null
    var isChairsNotEmpty:Boolean?=null
    var isTrashFull:Boolean?=null
    var isDoorOpen:Boolean?=null
    var isLedOneLighting:Boolean?=null
    var isLedTwoLighting:Boolean?=null
    var Temperature:Long?=null
    var Humidity:Long?=null
    constructor(IP:String,isManually:Boolean,isNight:Boolean,isWaterON:Boolean,isDrySoil:Boolean,isChairsNotEmpty:Boolean,
                isTrashFull:Boolean,isDoorOpen:Boolean,isLedOneLighting:Boolean,isLedTwoLighting:Boolean
                ,Temperature:Long,Humidity:Long){
        this.IP=IP
        this.isManually=isManually
        this.isNight=isNight
        this.isWaterON=isWaterON
        this.isDrySoil=isDrySoil
        this.isChairsNotEmpty=isChairsNotEmpty
        this.isTrashFull=isTrashFull
        this.isDoorOpen=isDoorOpen
        this.isLedOneLighting=isLedOneLighting
        this.isLedTwoLighting=isLedTwoLighting
        this.Temperature=Temperature
        this.Humidity=Humidity
    }

    constructor()

}