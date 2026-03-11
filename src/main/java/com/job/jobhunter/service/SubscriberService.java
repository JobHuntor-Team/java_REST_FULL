package com.job.jobhunter.service;

import com.job.jobhunter.domain.Skill;
import com.job.jobhunter.domain.Subscriber;
import com.job.jobhunter.domain.User;
import com.job.jobhunter.repository.SkillRepository;
import com.job.jobhunter.repository.SubscriberRepository;
import com.job.jobhunter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SubscriberService(SubscriberRepository subscriberRepository,SkillRepository skillRepository,
                             UserRepository userRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public Subscriber createSubscriber(Subscriber subscriberDetails) {
        //check email
        User user = userRepository.findByEmail(subscriberDetails.getEmail());
        if(user != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        //check list skill
        if(subscriberDetails.getSkills() != null) {
            List<Long> reqSkills = subscriberDetails.getSkills()
                    .stream().map(item -> item.getId())
                    .toList();

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriberDetails.setSkills(dbSkills);
        }
        // save subscriber
        return subscriberRepository.save(subscriberDetails);
    }


}
