package prototype.immutables_plus_mongodb;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Optional;
import org.immutables.mongo.repository.RepositorySetup;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String uri = "mongodb://localhost/test";
        RepositorySetup configuration = RepositorySetup.forUri(uri);

        // Instantiate generated repository
        ItemRepository items = new ItemRepository(configuration);

        // Clear all previously created items
        items.findAll().deleteAll().get();

        // Create item
        Item item = ImmutableItem.builder()
                .id(1)
                .name("one")
                .addValues(1, 2)
                .build();

        // Insert sync
        Integer insertCount = items.insert(item).get();
        System.out.println("insert: " + insertCount);

        Optional<Item> modifiedItem = items.findById(item.id())

                // findAndModify
                .andModifyFirst()

                // $addToSet
                .addValues(1)

                // $set
                .setComment("present")

                .returningNew()

                // returns future
                .update()

                // unsafe
                .getUnchecked();

        System.out.println("modifiedItem: " + modifiedItem);

        // Update all matching documents
        Integer updateCount = items.update(
                items.criteria()
                        .idIn(1, 2, 3)
                        .nameNot("Nameless")
                        .valuesNonEmpty())
                .emptyComment()
                .updateAll()
                .get();

        System.out.println("update: " + updateCount);

        Optional<Item> foundItem = items.findById(1).fetchFirst().get();
        System.out.println("found: " + foundItem);
    }
}
