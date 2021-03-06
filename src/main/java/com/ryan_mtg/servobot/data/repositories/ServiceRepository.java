package com.ryan_mtg.servobot.data.repositories;

import com.ryan_mtg.servobot.data.models.ServiceRow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceRow, Integer> {
    ServiceRow findByType(int type);
}
