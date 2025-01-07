package com.liutyk.first_demo.services;

import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final SessionSpeakersRepository sessionSpeakersRepository;

    @Autowired
    public SpeakerService(SpeakerRepository speakerRepository, SessionSpeakersRepository sessionSpeakersRepository) {
        this.speakerRepository = speakerRepository;
        this.sessionSpeakersRepository = sessionSpeakersRepository;
    }

    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }

    public Speaker getSpeakerById(Long id) throws SpeakerNotFoundException{

        return speakerRepository.findById(id)
                .orElseThrow(() -> new SpeakerNotFoundException(id));
    }

    public List<Speaker> getSpeakerByKeywordIgnoreCase(String keyword) {
        return speakerRepository.getSpeakerByKeywordIgnoreCase(keyword);
    }

    public List<Speaker> getSpeakerBySessionId(Long sessionId) {
        return speakerRepository.getSpeakerBySessionId(sessionId);
    }

    public Speaker postSpeaker(Speaker speaker) {
        validateSpeakerFields(speaker);
        return speakerRepository.saveAndFlush(speaker);
    }

    public Speaker putSpeaker(Long id, Speaker speaker) throws SpeakerNotFoundException{
        validateSpeakerFields(speaker);
        Optional<Speaker> optionalSpeaker = speakerRepository.findById(id);
        if (optionalSpeaker.isEmpty()) {
            throw new SpeakerNotFoundException(id);
        }
        Speaker existingSpeaker = optionalSpeaker.get();
        existingSpeaker.setFirstName(speaker.getFirstName());
        existingSpeaker.setLastName(speaker.getLastName());
        existingSpeaker.setTitle(speaker.getTitle());
        existingSpeaker.setCompany(speaker.getCompany());
        existingSpeaker.setSpeakerBio(speaker.getSpeakerBio());
        return speakerRepository.saveAndFlush(existingSpeaker);
    }
    public Speaker patchSpeaker(Long id, Speaker speaker) throws SpeakerNotFoundException{
        Optional<Speaker> optional = speakerRepository.findById(id);
        if (optional.isEmpty()) {
            throw new SpeakerNotFoundException(id);
        }
        Speaker existingSpeaker = optional.get();
        if (speaker.getFirstName() != null && !speaker.getFirstName().isBlank()) {
            existingSpeaker.setFirstName(speaker.getFirstName());
        }
        if (speaker.getLastName() != null  && !speaker.getLastName().isBlank()) {
            existingSpeaker.setLastName(speaker.getLastName());
        }
        if (speaker.getTitle() != null && !speaker.getTitle().isBlank()) {
            existingSpeaker.setTitle(speaker.getTitle());
        }
        if (speaker.getCompany() != null && !speaker.getCompany().isBlank()) {
            existingSpeaker.setCompany(speaker.getCompany());
        }
        if (speaker.getSpeakerBio() != null && !speaker.getSpeakerBio().isBlank()) {
            existingSpeaker.setSpeakerBio(speaker.getSpeakerBio());
        }
        return speakerRepository.saveAndFlush(existingSpeaker);
    }



    public void deleteSpeaker(Long id) throws SpeakerNotFoundException{
        if (!speakerRepository.existsById(id)) {
            throw new SpeakerNotFoundException(id);
        }
        sessionSpeakersRepository.deleteBySpeakerId(id);
        speakerRepository.deleteById(id);
    }


    private void validateSpeakerFields(Speaker speaker) {
        if (speaker.getFirstName() == null || speaker.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name must not be blank");
        }
        if (speaker.getLastName() == null || speaker.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name must not be blank");
        }
    }
}
