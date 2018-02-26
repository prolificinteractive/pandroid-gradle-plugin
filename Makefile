
plugin:
	./gradlew clean :pandroid-plugin:uploadArchives --console plain --no-build-cache

commitCheck: plugin
	./gradlew commitCheck --console plain

vcsCheck: plugin
	./gradlew vcsCheck --console plain
