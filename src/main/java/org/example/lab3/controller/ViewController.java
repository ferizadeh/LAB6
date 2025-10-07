import org.example.lab3.entities.BuddyInfo;
import org.example.lab3.entities.AddressBook;
import org.example.lab3.repository.BuddyInfoRepository;
import org.example.lab3.repository.AddressBookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class ViewController {

    private final AddressBookRepository addressBookRepository;
    private final BuddyInfoRepository buddyInfoRepository;

    public ViewController(AddressBookRepository addressBookRepository, BuddyInfoRepository buddyInfoRepository) {
        this.addressBookRepository = addressBookRepository;
        this.buddyInfoRepository = buddyInfoRepository;
    }

    // existing methods...

    @PostMapping("/addressbooks/{id}/buddies")
    public String addBuddy(
            @PathVariable Long id,
            @RequestParam String name,
            Model model) {

        AddressBook ab = addressBookRepository.findById(id).orElse(null);
        if (ab == null) {
            // handle error, e.g. redirect to home with error message or 404 page
            return "redirect:/";
        }

        BuddyInfo buddy = new BuddyInfo();
        buddy.setName(name);
        buddyInfoRepository.save(buddy);

        ab.addBuddy(buddy);
        addressBookRepository.save(ab);

        return "redirect:/addressbooks/" + id; // redirect back to the same page
    }
}
