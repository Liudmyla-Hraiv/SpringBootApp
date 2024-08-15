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

    public Optional<Speaker> getSpeakerById(Long id) {
        return speakerRepository.findById(id);
    }

    public List<Speaker> getByKeywordIgnoreCase(String keyword) {
        return speakerRepository.getByKeywordIgnoreCase(keyword);
    }

    public List<Speaker> getBySessionId(Long sessionId) {
        return speakerRepository.getBySessionId(sessionId);
    }

    public Speaker postSpeaker(Speaker speaker) {
        return speakerRepository.saveAndFlush(speaker);
    }

    public Speaker putSpeaker(Long id, Speaker speaker) {
        Optional<Speaker> optionalSpeaker = speakerRepository.findById(id);
        if (optionalSpeaker.isEmpty()) {
            throw new RuntimeException("Speaker with ID = " + id + " is not found");
        }
        Speaker existingSpeaker = optionalSpeaker.get();
        existingSpeaker.setFirstName(speaker.getFirstName());
        existingSpeaker.setLastName(speaker.getLastName());
        existingSpeaker.setTitle(speaker.getTitle());
        existingSpeaker.setCompany(speaker.getCompany());
        existingSpeaker.setSpeakerBio(speaker.getSpeakerBio());
        return speakerRepository.saveAndFlush(existingSpeaker);
    }
    public Speaker patchSpeaker(Long id, Speaker speaker) {
        Optional<Speaker> optional = speakerRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Speaker with ID " + id + " not found");
        }
        Speaker existingSpeaker = optional.get();
        if (speaker.getFirstName() != null && !speaker.getFirstName().isEmpty()) {
            existingSpeaker.setFirstName(speaker.getFirstName());
        }
        if (speaker.getLastName() != null && !speaker.getLastName().isEmpty()) {
            existingSpeaker.setLastName(speaker.getLastName());
        }
        if (speaker.getTitle() != null && !speaker.getTitle().isEmpty()) {
            existingSpeaker.setTitle(speaker.getTitle());
        }
        if (speaker.getCompany() != null && !speaker.getCompany().isEmpty()) {
            existingSpeaker.setCompany(speaker.getCompany());
        }
        if (speaker.getSpeakerBio() != null && !speaker.getSpeakerBio().isEmpty()) {
            existingSpeaker.setSpeakerBio(speaker.getSpeakerBio());
        }
        return speakerRepository.saveAndFlush(existingSpeaker);
    }



    public void deleteSpeaker(Long id) {
        if (!speakerRepository.existsById(id)) {
            throw new RuntimeException("Speaker with ID = " + id + " does not exist");
        }
        sessionSpeakersRepository.deleteBySpeakerId(id);
        speakerRepository.deleteById(id);
    }
}
