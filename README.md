# playerstats-refactored
This project is meant for Minecraft server owners to quickly and easily extrapolate playerstats from the command line
without having to dig through files, decipher UUIDs, etc.

This is the first iteration of this project. Currently it only supports obtaining playtime in hours for all players.
It also currently only supports servers with *ONE* world save. So, it will not work effectively for multi-world servers.

To use the the .jar, put it in the main directory with the rest of your server files and run the command:

`java -jar stats.jar`

![4bbdd512bc1dec075446e209a220d1ac](https://user-images.githubusercontent.com/73813963/225777018-ce5615a5-a6d5-4026-abf2-f36b4c8db220.gif)

It works for both iterations of Minecraft's stat trackers, as such it will ask the server admin what version of the game their server is.

Answer "y" if the server is 1.12 or older.
Answer "n" if the server is 1.13 or newer.

Planned features:
- Set server version once and skip the version prompt on subsequent runs
- Ability to choose what stats to show
- Store results in their own file with date for later reference.
