SRC_DIR = src
OUTPUT_DIR = out/relatorios
TEST_DIR = test-model
BIN_DIR = bin
JUNIT = lib/junit.jar
HAMCREST = lib/hamcrest-core.jar
CHECKSTYLE = lib/checkstyle.jar
ASSETS = $(SRC_DIR)/assets
TEST_FILES := $(shell find $(TEST_DIR) -name '*Test.java' | sed 's|$(TEST_DIR)/||' | sed 's|\.java$$||' | sed 's|/|.|g')

check_format:
	@-java -jar $(CHECKSTYLE) -c checkstyle.xml $(SRC_DIR)/
	@-java -jar $(CHECKSTYLE) -c checkstyle.xml -f xml -o $(OUTPUT_DIR)/checkstyle-report.xml $(SRC_DIR)/
	@echo "====== Checkstyle analysis completed. Report generated at $(OUTPUT_DIR)/checkstyle-report.xml ======"

compile:
	@echo "Compiling source files..."
	@javac -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SRC_DIR)/**/*.java
	@mkdir -p $(BIN_DIR)/assets
	@cp -r $(ASSETS)/* $(BIN_DIR)/assets/
	@echo "Compilation completed! Classes are in the folder $(BIN_DIR)"

compile_test: compile
	@echo "Compiling test files..."
	@javac -cp "$(JUNIT):$(HAMCREST)" -d $(BIN_DIR) -sourcepath $(SRC_DIR):$(TEST_DIR) $(SRC_DIR)/**/*.java $(TEST_DIR)/**/*.java
	@echo "Test compilation completed!"

test: compile_test
	@echo "Running tests..."
	@java -cp "$(BIN_DIR):$(JUNIT):$(HAMCREST)" org.junit.runner.JUnitCore $(TEST_FILES)
	@echo "Tests completed!"
