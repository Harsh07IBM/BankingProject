// Task 17 (Coding 1 + 2) - SUPPORT bounded context
// Same real world person, completely different model.
// In the Support context a "Customer" means someone who raises tickets.
// Support does not know or care about credit limit or sales region.

package support;

public class Customer {

    private final String customerId;
    private String contactPerson;
    private String supportPlan;      // BASIC / PREMIUM - meaningful only here
    private int openTickets;
    private int satisfactionScore;

    public Customer(String customerId,
                    String contactPerson,
                    String supportPlan) {

        this.customerId = customerId;
        this.contactPerson = contactPerson;
        this.supportPlan = supportPlan;
        this.openTickets = 0;
        this.satisfactionScore = 100;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getSupportPlan() {
        return supportPlan;
    }

    public int getOpenTickets() {
        return openTickets;
    }

    public int getSatisfactionScore() {
        return satisfactionScore;
    }

    // Support specific business rule
    public void raiseTicket() {
        this.openTickets++;
    }

    public void closeTicket(int score) {

        if (openTickets == 0) {
            throw new IllegalStateException("No open ticket to close");
        }

        this.openTickets--;
        this.satisfactionScore = score;
    }

    public int responseTimeInHours() {
        return "PREMIUM".equalsIgnoreCase(supportPlan) ? 2 : 24;
    }

    @Override
    public String toString() {
        return "support.Customer{id=" + customerId +
                ", contact=" + contactPerson +
                ", plan=" + supportPlan +
                ", openTickets=" + openTickets + "}";
    }
}
