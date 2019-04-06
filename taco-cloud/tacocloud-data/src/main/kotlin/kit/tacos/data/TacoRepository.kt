package kit.tacos.data

import kit.tacos.domain.Taco
import org.springframework.data.repository.PagingAndSortingRepository

interface TacoRepository : PagingAndSortingRepository<Taco, Long>