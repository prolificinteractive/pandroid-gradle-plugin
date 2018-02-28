
clean:
	./gradlew clean

plugin:
	./gradlew :pandroid-plugin:uploadArchives                         \
	-c plugin-settings.gradle                                         \
	--no-build-cache

run: plugin
	./gradlew commitCheck vcsCheck alphaBuild betaBuild releaseBuild  \
	--no-build-cache

test: plugin
	./gradlew :pandroid-plugin:test                                   \
	--no-build-cache
