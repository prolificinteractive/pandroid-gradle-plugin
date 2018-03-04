
clean:
	./gradlew clean

plugin:
	./gradlew :pandroid-plugin:uploadArchives -c settings-local.gradle

run: plugin
	./gradlew commitCheck vcsCheck ciBuild

test: clean plugin
	./gradlew :pandroid-plugin:test
