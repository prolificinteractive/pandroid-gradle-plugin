
clean:
	./gradlew clean

plugin:
	./gradlew :pandroid-plugin:uploadArchives                         \
	--no-build-cache

run: plugin
	./gradlew commitCheck vcsCheck ciBuild                            \
	--no-build-cache

test: clean plugin
	./gradlew :pandroid-plugin:test                                   \
	--no-build-cache
