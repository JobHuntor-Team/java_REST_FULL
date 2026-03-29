package com.job.jobhunter.service;

import com.job.jobhunter.domain.Skill;
import com.job.jobhunter.domain.Subscriber;
import com.job.jobhunter.repository.SkillRepository;
import com.job.jobhunter.repository.SubscriberRepository;
import com.job.jobhunter.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    // 1. TẠO MỚI (Create)
    public Subscriber createSubscriber(Subscriber subscriberDetails) throws IdInvalidException {
        boolean isExist = this.subscriberRepository.existsByEmail(subscriberDetails.getEmail());
        if (isExist) {
            throw new IdInvalidException("Email " + subscriberDetails.getEmail() + " đã tồn tại trong hệ thống.");
        }

        if (subscriberDetails.getSkills() != null && !subscriberDetails.getSkills().isEmpty()) {
            List<Long> reqSkills = subscriberDetails.getSkills()
                    .stream().map(Skill::getId)
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriberDetails.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subscriberDetails);
    }

    // 2. CẬP NHẬT (Update) - Đã chống lỗi Hibernate
    public Subscriber updateSubscriber(Subscriber sub) {
        Subscriber currentSub = this.subscriberRepository.findById(sub.getId()).orElse(null);
        if (currentSub != null) {
            currentSub.setName(sub.getName());
            currentSub.setEmail(sub.getEmail());

            if (sub.getSkills() != null) {
                List<Long> reqSkills = sub.getSkills()
                        .stream().map(Skill::getId)
                        .collect(Collectors.toList());
                List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);

                if (currentSub.getSkills() != null) {
                    currentSub.getSkills().clear();
                    currentSub.getSkills().addAll(dbSkills);
                } else {
                    currentSub.setSkills(dbSkills);
                }
            } else {
                if (currentSub.getSkills() != null) {
                    currentSub.getSkills().clear();
                }
            }
            return this.subscriberRepository.save(currentSub);
        }
        return null;
    }

    // 3. XÓA (Delete)
    public void deleteSubscriber(long id) {
        this.subscriberRepository.deleteById(id);
    }

    // 4. LẤY DANH SÁCH (Get All)
    public List<Subscriber> getAllSubscribers() {
        return this.subscriberRepository.findAll();
    }

    // 5. LẤY THÔNG TIN 1 NGƯỜI DÙNG (Get by ID)
    public Subscriber getSubscriberById(long id) {
        // Tìm kiếm trong DB, nếu không có thì trả về null
        return this.subscriberRepository.findById(id).orElse(null);
    }
}