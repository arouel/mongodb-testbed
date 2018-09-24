package prototype.todoapp;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import prototype.todoapp.command.ResetCollections;

class TodoAppEnv
        implements
        AfterAllCallback,
        AfterEachCallback,
        BeforeAllCallback,
        BeforeEachCallback {

    private TodoApp _app;

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        _app.command(ResetCollections.of()).get();
    }

    TodoApp app() {
        return _app;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        _app = TodoApp
                .builder()
                .mongoClientUri("mongodb://localhost/test")
                .build();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
    }
}
