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

    // GET handler to display AddressBook page
    @GetMapping("/addressbooks/{id}")
    public String showAddressBook(@PathVariable Long id, Model model) {
        AddressBook ab = addressBookRepository.findById(id).orElse(null);
        if (ab == null) {
            return "redirect:/"; // Or return a custom error page if you want
        }
        model.addAttribute("addressBook", ab);
        return "addressbook"; // Name of your Thymeleaf template (addressbook.html)
    }

    // POST handler to add a Buddy to the AddressBook
    @PostMapping("/addressbooks/{id}/buddies")
    public String addBuddy(
            @PathVariable Long id,
            @RequestParam String name,
            Model model) {

        AddressBook ab = addressBookRepository.findById(id).orElse(null);
        if (ab == null) {
            return "redirect:/";
        }

        BuddyInfo buddy = new BuddyInfo();
        buddy.setName(name);
        buddyInfoRepository.save(buddy);

        ab.addBuddy(buddy);
        addressBookRepository.save(ab);

        return "redirect:/addressbooks/" + id;
    }

    // Optional: POST handler to remove a Buddy from the AddressBook via UI
    @PostMapping("/addressbooks/{id}/buddies/{buddyId}/remove")
    public String removeBuddy(
            @PathVariable Long id,
            @PathVariable Long buddyId) {

        AddressBook ab = addressBookRepository.findById(id).orElse(null);
        BuddyInfo buddy = buddyInfoRepository.findById(buddyId).orElse(null);

        if (ab == null || buddy == null) {
            return "redirect:/";
        }

        ab.removeBuddy(buddy);
        buddyInfoRepository.delete(buddy);
        addressBookRepository.save(ab);

        return "redirect:/addressbooks/" + id;
    }
}
