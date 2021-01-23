package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {
    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping(value = "/ships")
    public List<Ship> getShips(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String planet,
                                @RequestParam(required = false) ShipType shipType,
                                @RequestParam(required = false) Long after,
                                @RequestParam(required = false) Long before,
                                @RequestParam(required = false) Boolean isUsed,
                                @RequestParam(required = false) Double minSpeed,
                                @RequestParam(required = false) Double maxSpeed,
                                @RequestParam(required = false) Integer minCrewSize,
                                @RequestParam(required = false) Integer maxCrewSize,
                                @RequestParam(required = false) Double minRating,
                                @RequestParam(required = false) Double maxRating,
                                @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
                                @RequestParam(defaultValue = "3", required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return this.shipService.gelAllShips(Specification
                .where(this.shipService.filterByName(name)
                .and(this.shipService.filterByPlanet(planet)))
                .and(this.shipService.filterByShipType(shipType))
                .and(this.shipService.filterByDate(after, before))
                .and(this.shipService.filterByUsage(isUsed))
                .and(this.shipService.filterBySpeed(minSpeed, maxSpeed))
                .and(this.shipService.filterByCrewSize(minCrewSize, maxCrewSize))
                .and(this.shipService.filterByRating(minRating, maxRating)), pageable);
    }

    @GetMapping(value = "/ships/count")
    public Long getCount(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String planet,
                         @RequestParam(required = false) ShipType shipType,
                         @RequestParam(required = false) Long after,
                         @RequestParam(required = false) Long before,
                         @RequestParam(required = false) Boolean isUsed,
                         @RequestParam(required = false) Double minSpeed,
                         @RequestParam(required = false) Double maxSpeed,
                         @RequestParam(required = false) Integer minCrewSize,
                         @RequestParam(required = false) Integer maxCrewSize,
                         @RequestParam(required = false) Double minRating,
                         @RequestParam(required = false) Double maxRating) {
        return this.shipService.getShipsCount(Specification
                .where(this.shipService.filterByName(name)
                .and(this.shipService.filterByPlanet(planet)))
                .and(this.shipService.filterByShipType(shipType))
                .and(this.shipService.filterByDate(after, before))
                .and(this.shipService.filterByUsage(isUsed))
                .and(this.shipService.filterBySpeed(minSpeed, maxSpeed))
                .and(this.shipService.filterByCrewSize(minCrewSize, maxCrewSize))
                .and(this.shipService.filterByRating(minRating, maxRating)));
    }

    @PostMapping(value = "/ships")
    public ResponseEntity<Ship> saveShip(@RequestBody Ship ship) {
        this.shipService.createShip(ship);

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @GetMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable("id") Long shipId) {

        Ship ship = this.shipService.getById(shipId);

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @PostMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> updateShip(@RequestBody Ship oldShip,
                                           @PathVariable("id") Long id) {
        Ship newShip = this.shipService.updateShip(id, oldShip);

        return new ResponseEntity<>(newShip, HttpStatus.OK);
    }

    @DeleteMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long shipId) {
        this.shipService.delete(shipId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
