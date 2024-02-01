package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Dummy;
import com.hei.wallet.heiwallet.repository.DummyRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class HealthService {
    private final DummyRepository dummyRepository;

    public List<Dummy> getAllDummies() {
        try {
            return dummyRepository.findAll();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public HealthService(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }
}
