# Recovery notes

The original `pet-api` directory was missing. This folder was reconstructed from VS Code local history and the saved Java learning session.

The recovered source is the last internally consistent CRUD version. A later local-history edit changed `PetRepository.findById()` to return `Optional<Pet>` without updating `PetService`; that mixed state was not used here because it would not compile.

The Maven wrapper and generated `target/` directory were not recovered. The project can use an installed `mvn` command, or the wrapper can be regenerated later.
