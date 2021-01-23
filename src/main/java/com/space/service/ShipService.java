package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ShipService {
    List<Ship> gelAllShips(Specification<Ship> specification, Pageable pageable);

    Long getShipsCount(Specification<Ship> specification);

    Ship getById(Long id);

    Ship createShip(Ship ship);

    Ship updateShip(Long id, Ship newShip);

    void delete(Long id);

    Specification<Ship> filterByName(String name);

    Specification<Ship> filterByPlanet(String planet);

    Specification<Ship> filterByShipType(ShipType shipType);

    Specification<Ship> filterByDate(Long after, Long before);

    Specification<Ship> filterByUsage(Boolean isUsed);

    Specification<Ship> filterBySpeed(Double min, Double max);

    Specification<Ship> filterByCrewSize(Integer min, Integer max);
    Specification<Ship> filterByRating(Double min, Double max);
}
