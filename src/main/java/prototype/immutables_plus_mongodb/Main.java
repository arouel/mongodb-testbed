package prototype.immutables_plus_mongodb;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.immutables.mongo.repository.RepositorySetup;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

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
        logger.info("insert: {}", insertCount);

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

        logger.info("modified item: {}", modifiedItem);

        // Update all matching documents
        Integer updateCount = items.update(
                items.criteria()
                        .idIn(1, 2, 3)
                        .nameNot("Nameless")
                        .valuesNonEmpty())
                .emptyComment()
                .updateAll()
                .get();

        logger.info("update: " + updateCount);

        Optional<Item> foundItem = items.findById(1).fetchFirst().get();
        logger.info("found: {}", foundItem);
    }
}
