//package kit.tacocloud.tacos.repositories
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.module.kotlin.convertValue
//import kit.tacocloud.tacos.domain.Order
//import kit.tacocloud.tacos.domain.Taco
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.jdbc.core.JdbcTemplate
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert
//import org.springframework.stereotype.Repository
//import java.util.Date
//import kotlin.collections.set
//
//@Repository
//class JdbcOrderRepository(
//        @Autowired private val jdbc: JdbcTemplate
//) : OrderRepository {
//    private val orderInserter = SimpleJdbcInsert(jdbc)
//            .withTableName("Taco_Order")
//            .usingGeneratedKeyColumns("id")
//
//    private val orderTacoInserter = SimpleJdbcInsert(jdbc)
//            .withTableName("Taco_Order_Tacos")
//
//    private val objectMapper = ObjectMapper()
//
//    override fun save(order: Order): Order {
//        order.placedAt = Date()
//        order.id = saveOrderDetails(order)
//        order.tacos.forEach {
//            saveTacoToOrder(it, order.id)
//        }
//        return order
//    }
//
//    private fun saveTacoToOrder(taco: Taco, orderId: Long) {
//        val values = HashMap<String, Any>()
//        values["tacoOrder"] = orderId
//        values["taco"] = taco.id
//        orderTacoInserter.execute(values)
//    }
//
//    private fun saveOrderDetails(order: Order): Long {
//        val values = objectMapper.convertValue<MutableMap<String, Any>>(order)
//        values["placedAt"] = order.placedAt
//        return orderInserter.executeAndReturnKey(values).toLong()
//    }
//}