package com.vikash.journalApp.controllers;

import com.vikash.journalApp.entity.JournalEntry;
import com.vikash.journalApp.entity.User;
import com.vikash.journalApp.srvices.JournalEntryService;
import com.vikash.journalApp.srvices.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;



        @GetMapping()
        public ResponseEntity<?> getAllJournalEntriesOfUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userByName = userService.findUserByName(authentication.getName());
            List<JournalEntry> allEntries = userByName.getJournalEntries();
            if(allEntries!=null && !allEntries.isEmpty())
            {
                return  new ResponseEntity<>(allEntries, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userByName = userService.findUserByName(authentication.getName());
        try {
            journalEntryService.saveJournalEntry(journalEntry,userByName.getUsername());
            return  new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/id/{journalID}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId journalID, @RequestBody JournalEntry journalEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userByName = userService.findUserByName(authentication.getName());
        List<JournalEntry> collect = userByName.getJournalEntries().stream()
                .filter(journal -> journal.getId().equals(journalID))
                .collect(Collectors.toList());
        if(!collect.isEmpty()) {
            JournalEntry updatedEntry = journalEntryService.updateEntry(journalID, journalEntry);
            if (updatedEntry != null) {
                return new ResponseEntity<>(updatedEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/id/{journalID}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId journalID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userByName = userService.findUserByName(authentication.getName());

         journalEntryService.deleteEntryByID(journalID,userByName.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/id/{journalID}")
    public ResponseEntity<?> getJournalEntryByID(@PathVariable ObjectId journalID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userByName = userService.findUserByName(authentication.getName());
        List<JournalEntry> collect = userByName.getJournalEntries().stream()
                .filter(journalEntry -> journalEntry.getId().equals(journalID))
                .collect(Collectors.toList());
        if(!collect.isEmpty()){
        JournalEntry journalEntry = journalEntryService.getJournalEntryById(journalID).orElse(null);
            if(journalEntry!=null)
            {
                return  new ResponseEntity<>(journalEntry, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
