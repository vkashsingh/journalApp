package com.vikash.journalApp.srvices;

import com.vikash.journalApp.entity.JournalEntry;
import com.vikash.journalApp.entity.User;
import com.vikash.journalApp.repositories.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository  journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveJournalEntry(JournalEntry journalEntry, String userName)
    {
        User user = userService.findUserByName(userName);

        journalEntry.setDate(LocalDateTime.now());
        JournalEntry entry = journalEntryRepository.save(journalEntry);

        user.getJournalEntries().add(entry);
        userService.saveUser(user);

    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public void deleteEntryByID(ObjectId journalID,String userName) {
        User user = userService.findUserByName(userName);

        journalEntryRepository.deleteById(journalID);

        user.getJournalEntries().removeIf(x->x.getId().equals(journalID));
        userService.saveUser(user);
    }

    public Optional<JournalEntry> getJournalEntryById(ObjectId journalID) {
        return  journalEntryRepository.findById(journalID);
    }

    public JournalEntry updateEntry(ObjectId journalID, JournalEntry journalEntry) {
        Optional<JournalEntry> existingEntryOpt = journalEntryRepository.findById(journalID);

        if (existingEntryOpt.isPresent()) {
            JournalEntry existingEntry = existingEntryOpt.get();

            if (!journalEntry.getContent().equals(existingEntry.getContent())) {
                existingEntry.setDate(LocalDateTime.now());
                existingEntry.setContent(journalEntry.getContent());
            }

            if (!journalEntry.getTitle().equals(existingEntry.getTitle())) {
                existingEntry.setDate(LocalDateTime.now());
                existingEntry.setTitle(journalEntry.getTitle());
            }

            return journalEntryRepository.save(existingEntry);
        }

        return null;
    }

}
