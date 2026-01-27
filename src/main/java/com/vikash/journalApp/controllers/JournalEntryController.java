package com.vikash.journalApp.controllers;

import com.vikash.journalApp.entity.JournalEntry;
import com.vikash.journalApp.entity.User;
import com.vikash.journalApp.srvices.JournalEntryService;
import com.vikash.journalApp.srvices.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<JournalEntry> allEntries = journalEntryService.getAllEntries();
        if(allEntries!=null && !allEntries.isEmpty())
        {
            return  new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
        @GetMapping("/{userName}")
        public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
            User userByName = userService.findUserByName(userName);
            List<JournalEntry> allEntries = userByName.getJournalEntries();
            if(allEntries!=null && !allEntries.isEmpty())
            {
                return  new ResponseEntity<>(allEntries, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    @PostMapping("/{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry,@PathVariable String userName) {
        try {
            journalEntryService.saveJournalEntry(journalEntry,userName);
            return  new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/id/{journalID}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId journalID, @RequestBody JournalEntry journalEntry) {
        JournalEntry updatedEntry = journalEntryService.updateEntry(journalID, journalEntry);
            if(updatedEntry!=null)
            {
                return new ResponseEntity<>(updatedEntry,HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/id/{userName}/{journalID}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId journalID,@PathVariable String userName) {
         journalEntryService.deleteEntryByID(journalID,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/id/{journalID}")
    public ResponseEntity<?> getJournalEntryByID(@PathVariable ObjectId journalID) {
        JournalEntry journalEntry = journalEntryService.getJournalEntryById(journalID).orElse(null);
        if(journalEntry!=null)
        {
            return  new ResponseEntity<>(journalEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
