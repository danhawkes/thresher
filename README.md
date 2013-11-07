# Grooveshark-api

An unofficial API for Grooveshark, in Java.

# Usage

First off, you'll need a client:

```java
client = new Client();
```

### Search

By keyword: 

```java
client.search("clair de lune");
```

For popular songs:

```java
client.searchPopularSongs();
```

### Stream

Get a short-lived song URL:

```java
client.getStreamUrl(Song);
```

### Login

```java
user = client.login("username", "hunter2");
```

### Library / Favorites

List contents: 

```java
user.library.get();
user.favorites.get();
```

Add/Remove songs:

```java
user.library.add(song);
user.library.remove(song);
user.favorites.add(song);
user.favorites.remove(song);
```

# Build / Test

To build and run tests against the real servers:

```shell
mvn clean install
```

# Contribute

Bug reports and pull requests are much appreciated.

# Thanks

To [sosedoff](https://github.com/sosedoff) and contibutors for providing API information in their [Ruby library](https://github.com/sosedoff/grooveshark).

# Licence

Apache 2.0. See `LICENCE.txt` for details.
