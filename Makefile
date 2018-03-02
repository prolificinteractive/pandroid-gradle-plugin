
clean:
	./gradlew clean

plugin:
	./gradlew :pandroid-plugin:uploadArchives

run: plugin
	./gradlew commitCheck vcsCheck ciBuild

test: clean plugin
	./gradlew :pandroid-plugin:test
