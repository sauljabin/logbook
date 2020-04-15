gradle=GIT_COMMIT=$(shell git rev-parse HEAD) ./gradlew clean

test:
	@ $(gradle) test $(opt)

clean:
	@ $(gradle)

jar:
	@ $(gradle) build

doc:
	@ $(gradle) javadoc

coverage:
	@ $(gradle) test jacocoTestReport jacocoTestCoverageVerification

run:
	@ $(gradle) run
