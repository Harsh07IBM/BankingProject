public class Day4_MyLinkedList {

    Item first;

    static class Item {
        int value;
        Item nextItem;

        Item(int value) {
            this.value = value;
            this.nextItem = null;
        }
    }

    public void addItem(int value) {

        Item newItem = new Item(value);

        if (first == null) {
            first = newItem;
            return;
        }

        Item currentItem = first;

        while (currentItem.nextItem != null) {
            currentItem = currentItem.nextItem;
        }

        currentItem.nextItem = newItem;
    }

    public void showItems() {

        Item currentItem = first;

        System.out.print("My Linked List: ");

        while (currentItem != null) {
            System.out.print(currentItem.value + " -> ");
            currentItem = currentItem.nextItem;
        }

        System.out.println("null");
    }

    public static void main(String[] args) {

        Day4_MyLinkedList numbers = new Day4_MyLinkedList();

        numbers.addItem(100);
        numbers.addItem(200);
        numbers.addItem(300);

        numbers.showItems();
    }
}
