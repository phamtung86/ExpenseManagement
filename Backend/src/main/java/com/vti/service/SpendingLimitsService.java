package com.vti.service;

import com.vti.dto.SpendingLimits.SpendingLimitsCreateRequest;
import com.vti.dto.SpendingLimits.SpendingLimitsDTO;
import com.vti.dto.SpendingLimits.SpendingLimitsUpdateRequest;
import com.vti.entity.SpendingLimits;
import com.vti.repository.ICategoriesRepository;
import com.vti.repository.IMoneySourceRepository;
import com.vti.repository.ISpendingLimitsRepository;
import com.vti.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpendingLimitsService implements ISpendingLimitsService {

    private final ISpendingLimitsRepository spendingLimitsRepository;
    private final ICategoriesRepository categoryRepository;
    private final IMoneySourceRepository moneySourceRepository;
    private final IUserRepository userRepository;

    @Override
    public List<SpendingLimitsDTO> getAll(int userId) {
        return spendingLimitsRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SpendingLimitsDTO getById(Integer id) {
        SpendingLimits entity = spendingLimitsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hạn mức với id = " + id));
        return toDTO(entity);
    }

    @Override
    public SpendingLimitsDTO create(SpendingLimitsCreateRequest request) {
        SpendingLimits entity = new SpendingLimits();
        entity.setLimitAmount(request.getLimitAmount());
        entity.setPeriodType(SpendingLimits.PeriodType.valueOf(request.getPeriodType().toUpperCase()));
        entity.setStartDate(request.getStartDate());
        entity.setNote(request.getNote());
        entity.setCreatedAt(new Date());
        entity.setUpdateAt(new Date());
        entity.setActualSpent(0.0);
        entity.setActive(true);

        entity.setCategories(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục")));
        entity.setMoneySources(moneySourceRepository.findById(request.getMoneySourceId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nguồn tiền")));

        entity.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng")));

        spendingLimitsRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public SpendingLimitsDTO update(Integer id, SpendingLimitsUpdateRequest request) {

        SpendingLimits entity = spendingLimitsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hạn mức"));

        entity.setLimitAmount(request.getLimitAmount());
        entity.setPeriodType(SpendingLimits.PeriodType.valueOf(request.getPeriodType().toUpperCase()));
        entity.setStartDate(request.getStartDate());
        entity.setNote(request.getNote());
        entity.setActive(request.isActive());
        entity.setUpdateAt(new Date());

        spendingLimitsRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public void delete(Integer id) {
        if (!spendingLimitsRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy hạn mức");
        }
        spendingLimitsRepository.deleteById(id);
    }

    @Override
    public SpendingLimits findByCategoriesIdAndMoneySourcesIdAndUserId(Integer categoriesId, Integer moneySourcesId, Integer userId) {
        return spendingLimitsRepository.findByCategoriesIdAndMoneySourcesIdAndUserId(categoriesId, moneySourcesId, userId);
    }

    @Override
    public void updateActualSpent(Integer id, Double actualSpent) {
        SpendingLimits entity = spendingLimitsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hạn mức"));
        entity.setActualSpent(actualSpent);
        entity.setUpdateAt(new Date());
        spendingLimitsRepository.save(entity);
    }

    private SpendingLimitsDTO toDTO(SpendingLimits entity) {
        return new SpendingLimitsDTO(
                entity.getId(),
                entity.getLimitAmount(),
                entity.getPeriodType().name(),
                entity.getStartDate(),
                entity.getActualSpent(),
                entity.getNote(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdateAt(),
                entity.getCategories().getId(),
                entity.getCategories().getName(),
                entity.getMoneySources().getId(),
                entity.getMoneySources().getName(),
                entity.getUser().getId(),
                entity.getUser().getFullName()
        );
    }
}
