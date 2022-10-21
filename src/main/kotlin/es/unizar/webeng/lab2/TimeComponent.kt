package es.unizar.webeng.lab2

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

data class TimeDTO(val time: LocalDateTime)
interface TimeProvider {
    fun now(): LocalDateTime
}

@Service
class TimeService : TimeProvider {
    override fun now() = LocalDateTime.now()
}
/**
 * Añadir una extension: Añadir un método estático.
 */
fun LocalDateTime.toDTO() = TimeDTO(time = this)
/**
 * Identificar como un endpoint
 */
@RestController
class TimeController(val service: TimeProvider) {
    @GetMapping("/time")
    fun time() = service.now().toDTO()
}
