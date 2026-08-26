package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.domain.model.PaymentPeriod
import com.jpt.todotogether.core.domain.model.Settings
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsRepositoryTest {

    @Test
    fun testSettingsModel() {
        // Test default settings
        val defaultSettings = Settings()
        assertEquals(PaymentPeriod.MONTHLY, defaultSettings.paymentPeriod)
        
        // Test custom settings
        val customSettings = Settings(paymentPeriod = PaymentPeriod.WEEKLY)
        assertEquals(PaymentPeriod.WEEKLY, customSettings.paymentPeriod)
    }
    
    @Test
    fun testPaymentPeriodEnum() {
        // Test all payment period values
        assertEquals(4, PaymentPeriod.entries.size)
        
        val periods = listOf(
            PaymentPeriod.WEEKLY,
            PaymentPeriod.BIWEEKLY,
            PaymentPeriod.MONTHLY,
            PaymentPeriod.YEARLY
        )
        
        periods.forEach { period ->
            assertEquals(period, PaymentPeriod.valueOf(period.name))
        }
    }
}
