package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {
    final ShipRepository shipRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Ship getById(Long id) {
        if (id <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return shipRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Ship createShip(Ship ship) {
        if (ship.getName() == null || ship.getName().isEmpty() || ship.getName().length() > 50 ||
            ship.getPlanet() == null || ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50 ||
            ship.getShipType() == null || ship.getProdDate() == null ||
            ship.getSpeed() == null || ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99 ||
            ship.getCrewSize() == null || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ship.getProdDate().getTime());
        int prodYear = calendar.get(Calendar.YEAR);

        if (prodYear < 2800 || prodYear > 3019)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (ship.getUsed() == null) ship.setUsed(false);

        double k = ship.getUsed() ? 0.5 : 1;
        double rating = 80 * ship.getSpeed() * k / (3019 - prodYear + 1);
        rating = Math.round(rating * 100) / 100.0;
        ship.setRating(rating);

        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public Ship updateShip(Long id, Ship newShip) {
        Ship oldShip = getById(id);

        int prodYear = 0;
        Calendar calendar = Calendar.getInstance();

        if (newShip.getName() != null)
            oldShip.setName(newShip.getName());
        if (newShip.getPlanet() != null)
            oldShip.setPlanet(newShip.getPlanet());
        if (newShip.getShipType() != null)
            oldShip.setShipType(newShip.getShipType());
        if (newShip.getProdDate() != null) {
            calendar.setTimeInMillis(newShip.getProdDate().getTime());
            oldShip.setProdDate(newShip.getProdDate());
        } else {
            calendar.setTimeInMillis(oldShip.getProdDate().getTime());
        }
        prodYear = calendar.get(Calendar.YEAR);
        if (newShip.getUsed() != null)
            oldShip.setUsed(newShip.getUsed());
        if (newShip.getSpeed() != null)
            oldShip.setSpeed(newShip.getSpeed());
        if (newShip.getCrewSize() != null)
            oldShip.setCrewSize(newShip.getCrewSize());

        if (oldShip.getName().length() > 50 || oldShip.getPlanet().length() > 50 ||
            oldShip.getName().isEmpty() || oldShip.getPlanet().isEmpty() ||
            oldShip.getSpeed() < 0.01 || oldShip.getSpeed() > 0.99 ||
            oldShip.getCrewSize() < 1 || oldShip.getCrewSize() > 9999 ||
            prodYear < 2800 || prodYear > 3019)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        double k = oldShip.getUsed() ? 0.5 : 1;
        double rating = 80 * oldShip.getSpeed() * k / (3019 - prodYear + 1);
        rating = Math.round(rating * 100) / 100.0;
        oldShip.setRating(rating);

        return oldShip;
    }

    @Override
    public void delete(Long id) {
        Ship ship = getById(id);
        shipRepository.delete(ship);
    }

    @Override
    public List<Ship> gelAllShips(Specification<Ship> specification, Pageable pageable) {
        return shipRepository.findAll(specification, pageable).getContent();
    }

    @Override
    public Long getShipsCount(Specification<Ship> specification) {
        return shipRepository.count(specification);
    }

    @Override
    public Specification<Ship> filterByName(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(root.get("name"), "%" + name + "%");
    }

    @Override
    public Specification<Ship> filterByPlanet(String planet) {
        return (root, query, builder) -> planet == null ? null : builder.like(root.get("planet"), "%" + planet + "%");
    }

    @Override
    public Specification<Ship> filterByShipType(ShipType shipType) {
        return (root, query, builder) -> shipType == null ? null : builder.equal(root.get("shipType"), shipType);
    }

    @Override
    public Specification<Ship> filterByDate(Long after, Long before) {
        return (root, query, builder) -> {
            if (after == null && before == null) {
                return null;
            }

            if (after == null) {
                return builder.lessThanOrEqualTo(root.get("prodDate"), new Date(before));
            }

            if (before == null) {
                return builder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after));
            }

            return builder.between(root.get("prodDate"), new Date(after), new Date(before));
        };
    }

    @Override
    public Specification<Ship> filterByUsage(Boolean isUsed) {
        return (root, query, builder) -> {
            if (isUsed == null) {
                return null;
            }

            if (isUsed) {
                return builder.isTrue(root.get("isUsed"));
            }

            return builder.isFalse(root.get("isUsed"));
        };
    }

    @Override
    public Specification<Ship> filterBySpeed(Double min, Double max) {
        return (root, query, builder) -> {
            if (min == null && max == null) {
                return null;
            }

            if (min == null) {
                return builder.lessThanOrEqualTo(root.get("speed"), max);
            }

            if (max == null) {
                return builder.greaterThanOrEqualTo(root.get("speed"), min);
            }

            return builder.between(root.get("speed"), min, max);
        };
    }

    @Override
    public Specification<Ship> filterByCrewSize(Integer min, Integer max) {
        return (root, query, builder) -> {
            if (min == null && max == null) {
                return null;
            }

            if (min == null) {
                return builder.lessThanOrEqualTo(root.get("crewSize"), max);
            }

            if (max == null) {
                return builder.greaterThanOrEqualTo(root.get("crewSize"), min);
            }

            return builder.between(root.get("crewSize"), min, max);
        };
    }

    @Override
    public Specification<Ship> filterByRating(Double min, Double max) {
        return (root, query, builder) -> {
            if (min == null && max == null) {
                return null;
            }

            if (min == null) {
                return builder.lessThanOrEqualTo(root.get("rating"), max);
            }

            if (max == null) {
                return builder.greaterThanOrEqualTo(root.get("rating"), min);
            }

            return builder.between(root.get("rating"), min, max);
        };
    }
}