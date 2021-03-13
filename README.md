# neo4j-friend-graph test

This project contains a simple data structure and a test class for it.

## The data structure

There is only one node: `User`. It has some simple properties and one relationship with both sides mapped in the entity: a *sent* friend request list, and a *received* friend request list.

## `UserRepositoryTest` structure

The `UserRepositoryTest` uses testcontainers. The tests are ordered and rollback is turned off, so the transactions are committed at the end of every test method.

## The problem

Running the `UserRepositoryTest` class has inconsistent outcomes. The last test method either runs successfully, or it fails. (It might take 5-10 runs to get both outcomes.)

The last test can fail because in *some* cases the `save` call in test with order 4 removes all previously saved relationships and only the currently saved friend request is saved.

There are two txt files in the `test-result-logs` folder: `failed.txt` for the logs of an unsuccessful run and `successful.txt` for the logs of a successful run.

## Running the tests

Either run the `UserRepositoryTest` class in an IDE or run `gradle repositoryTest` in the project.