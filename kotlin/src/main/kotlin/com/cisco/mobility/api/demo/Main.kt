package com.cisco.mobility.api.demo

import kotlin.system.exitProcess

const val SMS_RECIPIENT = "+4799999901"
const val SMS_SENDER = "SuperSender"
const val SMS_CONTENT = "This is a test message"

data class Config(
    val apiClientId: String,
    val apiClientSecret: String,
    val apiTarget: String = "api.mobility.cisco.com:443",
    val requiredScopes: Set<String> = setOf("sms.text:send_to_subscriber"),
)

fun main() {
    println("Starting...")

    val clientId = System.getenv("CLIENT_ID") ?: throw IllegalArgumentException("Missing CLIENT_ID")
    val clientSecret = System.getenv("CLIENT_SECRET") ?: throw IllegalArgumentException("Missing CLIENT_SECRET")

    val config = Config(clientId, clientSecret)

    val grpcUtils = GrpcUtils(config)
    val stub = grpcUtils.getSmsBlockingStub()
    val token = grpcUtils.getToken()
    println("Auth token requested successfully")

    val smsService = SmsService(stub, token)
    val smsRequest = SmsRequest(
        recipient = MsisdnSmsRecipient(SMS_RECIPIENT),
        fromAddress = SMS_SENDER,
        content = SMS_CONTENT
    )
    println("Trying to send sms:")
    println(smsRequest)
    try {
        val response = smsService.sendSms(smsRequest)
        println("Response: $response")
    } catch (e: Exception) {
        println("Error: ${e.localizedMessage}")
    }
    println("\nGoodbye...")
    exitProcess(0)
}
