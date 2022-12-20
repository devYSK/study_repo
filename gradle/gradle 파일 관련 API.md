## Gradle 파일 관련 API를 제공하는 이유

거의 모든 Gradle 빌드는 소스 파일, 파일 종속성, 보고서 등과 같은 방식으로 파일과 상호 작용합니다. 이것이 Gradle에 필요한 파일 작업을 간단하게 수행 할 수 있도록 도와줍니다.

*  ◆ Gradle 파일 관련 API를 제공하는 이유
* ◆ Gradle Build Script - 파일 복사 (한개)
* ◆ Gradle Build Script - 파일 복사 (여러개)
* ◆ Gradle Build Script - 폴더 복사
* ◆ Gradle Build Script - 압축파일 만들기
* ◆ Gradle Build Script - 압축풀기
* ◆ Gradle Build Script - uber / fat JAR 파일 만들기
* ◆ Gradle Build Script - 폴더 만들기
* ◆ Gradle Build Script - 파일/폴더 이동
* ◆ Gradle Build Script - 복사 후 파일 이름 바꾸기
* ◆ Gradle Build Script - 파일 및 폴더 삭제
* ◆ Gradle Build Script - 파일 경로 하드코딩 줄이기
* ◆ Gradle Build Script - 파일 객체
* ◆ Gradle Build Script - 파일 콜렉션
* ◆ Gradle Build Script - 파일 트리
* ◆ Gradle Build Script - 컴파일
* ◆ Gradle Build Script - 좀 더 복잡한 필터링]

 

## ◆ Gradle Build Script - 파일 복사 (한개)

Gradle의 내장 복사 작업 인스턴스를 생성하고 파일의 위치와 파일을 저장할 위치로 구성하여 파일을 복사 합니다.

------

\# Ex 1

file("경로") 의 형태로 복사할 파일의 경로와 붙여넣기할 폴더의 경로를 지정합니다.

 

<< Groovy >>

```groovy
task copyReport(type: Copy) {
    from file("$buildDir/reports/my-report.pdf")
    into file("$buildDir/toArchive")
}
```



------

\# Ex 2

"경로" 의 형태로 복사할 파일의 경로와 붙여넣기할 폴더의 경로를 지정합니다.

 

<< Groovy >>

```groovy
task copyReport2(type: Copy) {
    from "$buildDir/reports/my-report.pdf"
    into "$buildDir/toArchive"
}
```

------

\# Ex 3

위에 있는 두개의 예제처럼 하드코딩이 아니라 프로퍼티를 참조하는 방식으로, 아래와 같은 방식을 추천합니다.

 

<< Groovy >>

```groovy
task copyReport3(type: Copy) {
    from myReportTask.outputFile
    into archiveReportsTask.dirToArchive
}
```

 

 

## ◆ Gradle Build Script - 파일 복사 (여러개)

\# Ex 1

from "파일경로1", "파일경로2" ... 과 같은 형식을 통해서 파일을 한개씩 추가한 후 복사를 합니다.

 

<< Groovy >>

```groovy
task copyReportsForArchiving(type: Copy) {
    from "$buildDir/reports/my-report.pdf", "src/docs/manual.pdf"
    into "$buildDir/toArchive"
}
```

------

\# Ex 2

include "*.pdf" 과 같이 확장자를 지정하여 해당하는 확장자를 가지는 파일을 복사합니다.

 

<< Groovy >>

```groovy
task copyPdfReportsForArchiving(type: Copy) {
    from "$buildDir/reports"
    include "*.pdf"
    into "$buildDir/toArchive"
}
```

------

\# Ex 3

include **/*.pdf 와 같이 from 폴더에 하위 폴더에 있는 pdf 파일도 전부 복사합니다.

 

<< Groovy >>

```groovy
task copyAllPdfReportsForArchiving(type: Copy) {
    from "$buildDir/reports"
    include "**/*.pdf"
    into "$buildDir/toArchive"
}
```

 

 

## ◆ Gradle Build Script - 폴더 복사

\# Ex 1

from "복사할 폴더경로"

into "붙여넣을 폴더경로"

reports 안에 있는 파일 및 폴더를 to Archve 에 복사합니다.

 

<< Groovy >>

```groovy
task copyReportsDirForArchiving(type: Copy) {
    from "$buildDir/reports"
    into "$buildDir/toArchive"
}
```

------

\# Ex 2

위와 다른점은 reports 폴더까지 복사됩니다.

 

<< Groovy >>

```groovy
task copyReportsDirForArchiving2(type: Copy) {
    from("$buildDir") {
        include "reports/**"
    }
    into "$buildDir/toArchive"
}
```

 

 

## ◆ Gradle Build Script - 압축파일 만들기

\# Ex 1

폴더를 ZIP 파일로 만들기

\- archiveFileName = "압축파일명.zip"

\- destinationDirectory = file("저장할 폴더")

\- from "압축할 폴더"

 

<< Groovy >>

```groovy
task packageDistribution(type: Zip) {
    archiveFileName = "my-distribution.zip"
    destinationDirectory = file("$buildDir/dist")

    from "$buildDir/toArchive"
}
```



------

\# Ex 2

pdf 파일을 별도로 압축해서 저장하려는 경우에 사용합니다

 

<< Groovy >>

```groovy
plugins {
    id 'base'
}

version = "1.0.0"

task packageDistribution(type: Zip) {
    from("$buildDir/toArchive") {
        exclude "**/*.pdf"
    }

    from("$buildDir/toArchive") {
        include "**/*.pdf"
        into "docs"
    }
}
```

 

 

## ◆ Gradle Build Script - 압축풀기

\# Ex 1

파일 압축풀기

\- zipTree("압축파일 경로")

\- into "압축을 해제할 폴더의 경로"

 

<< Groovy >>

```groovy
task unpackFiles(type: Copy) {
    from zipTree("src/resources/thirdPartyResources.zip")
    into "$buildDir/resources"
}
```

------

\# Ex 2

압축을 푸는 경우에 libs 안에 있는 압축파일들의 압축만 풀 수 있도록 include 옵션을 사용합니다.

 

<< Groovy >>

```groovy
task unpackLibsDirectory(type: Copy) {
    from(zipTree("src/resources/thirdPartyResources.zip")) {
        include "libs/**"  
        eachFile { fcd ->
            fcd.relativePath = new RelativePath(true, fcd.relativePath.segments.drop(1))  
        }
        includeEmptyDirs = false  
    }
    into "$buildDir/resources"
}
```

 

 

## ◆ Gradle Build Script - uber / fat JAR 파일 만들기

uber / fat JAR - 모든 의존성에 있는 라이브러리가 자체 포함되어 있는 **JAR** 파일

 

\# Ex 1

이 경우 프로젝트의 런타임 종속성을 가져 와서 configurations.runtimeClasspath.files각 JAR 파일을 zipTree()메소드로 래핑합니다 . 결과는 ZIP 파일 트리 모음으로, 그 내용은 응용 프로그램 클래스와 함께 uber JAR로 복사됩니다.

 

<< Groovy >>

```groovy
plugins {
    id 'java'
}

version = '1.0.0'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'commons-io:commons-io:2.6'
}

task uberJar(type: Jar) {
    archiveClassifier = 'uber'

    from sourceSets.main.output

    dependsOn configurations.runtimeClasspath
    from {
        configurations.runtimeClasspath.findAll { it.name.endsWith('jar') }.collect { zipTree(it) }
    }
}
```

 

## ◆ Gradle Build Script - 폴더 만들기

\# Ex 1

mkdr "폴더명" 을 이용해서 폴더를 생성합니다.

 

<< Groovy >>

```groovy
task ensureDirectory {
    doLast {
        mkdir "images"
    }
}
```

 

 

## ◆ Gradle Build Script - 파일/폴더 이동

\# Ex 1

이것은 일반적인 요구 사항이 아니며 정보를 잃어 쉽게 빌드를 중단 할 수 있으므로 드물게 사용해야합니다.

ant.move file: "이전 위치"

ant.move todir: "이동할 위치"

 

<< Groovy >>

```groovy
task moveReports {
    doLast {
        ant.move file: "${buildDir}/reports",
                 todir: "${buildDir}/toArchive"
    }
}
```

 

 

## ◆ Gradle Build Script - 복사 후 파일 이름 바꾸기

\# Ex 1

"-staging-"마커가있는 파일 이름에서 마커를 제거합니다.

 

<< Groovy >>

```groovy
task copyFromStaging(type: Copy) {
    from "src/main/webapp"
    into "$buildDir/explodedWar"

    rename '(.+)-staging(.+)', '$1$2'
}
```

------

\# Ex 2

파일 이름을 7 글자로 잘라서 저장합니다.

 

<< Groovy >>

```groovy
task copyWithTruncate(type: Copy) {
    from "$buildDir/reports"
    rename { String filename ->
        if (filename.size() > 10) {
            return filename[0..7] + "~" + filename.size()
        }
        else return filename
    }
    into "$buildDir/toArchive"
}
```

 

 

## ◆ Gradle Build Script - 파일 및 폴더 삭제

\# Ex 1

빌드 출력 폴더의 전체 내용을 삭제합니다.

 

<< Groovy >>

```groovy
task myClean(type: Delete) {
    delete buildDir
}
```

------

\# Ex 2

특정 패턴을 가지는 파일들을 제거합니다. (.tmp 임시파일)

 

<< Groovy >>

```groovy
task cleanTempFiles(type: Delete) {
    delete fileTree("src").matching {
        include "**/*.tmp"
    }
}
```

 

 

## ◆ Gradle Build Script - 파일 경로 하드코딩 줄이기

\# Ex 1

ext 블록에 변수로 경로를 설정한 다음에 그 변수를 이용하여 task 를 생성합니다.

 

<< Groovy >>

```groovy
ext {
    archivesDirPath = "$buildDir/archives"
}

task packageClasses(type: Zip) {
    archiveAppendix = "classes"
    destinationDirectory = file(archivesDirPath)

    from compileJava
}
```

 

 

## ◆ Gradle Build Script - 파일 객체

\# Ex 1

파일을 찾는 방법을 모두 나열하였습니다.

원하는 방법으로 찾으시면 될 것 같습니다.

 

<< Groovy >>

```groovy
// Using a relative path
File configFile = file('src/config.xml')

// Using an absolute path
configFile = file(configFile.absolutePath)

// Using a File object with a relative path
configFile = file(new File('src/config.xml'))

// Using a java.nio.file.Path object with a relative path
configFile = file(Paths.get('src', 'config.xml'))

// Using an absolute java.nio.file.Path object
configFile = file(Paths.get(System.getProperty('user.home')).resolve('global-config.xml'))
```

------

\# Ex 2

상위 프로젝트와 관련된 경로로 파일을 불러옵니다.

 

<< Groovy >>

```groovy
File configFile = file("$rootDir/shared/config.xml")
```

 

 

## ◆ Gradle Build Script - 파일 콜렉션

\# Ex 1

파일 콜렉션을 작성하는 방법입니다.

 

```
layout.files(

  '파일경로',

  new File('파일경로'),

  ['파일경로', '파일경로'],

  Paths.get('폴더경로', '파일명')

)
```

 

<< Groovy >>

```groovy
FileCollection collection = layout.files(
  'src/file1.txt',
  new File('src/file2.txt'),
  ['src/file3.csv', 'src/file4.csv'],
  Paths.get('src', 'file5.txt')
)
```

------

\# Ex 2

폴더로부터 파일 콜렉션을 생성해서 사용하기

 

<< Groovy >>

```groovy
task list {
    doLast {
        File srcDir

        // Create a file collection using a closure
        collection = layout.files { srcDir.listFiles() }

        srcDir = file('src')
        println "Contents of $srcDir.name"
        collection.collect { relativePath(it) }.sort().each { println it }

        srcDir = file('src2')
        println "Contents of $srcDir.name"
        collection.collect { relativePath(it) }.sort().each { println it }
    }
}
```

 

<< Command >>

```
> gradle -q list
Contents of src
src/dir1
src/file1.txt
Contents of src2
src2/dir1
src2/dir2
```

------

\# Ex 3

파일 콜렉션을 사용하는 방법입니다.

\- collection.files / collection as Set

\- collection as List

\- collection.asPath

\- collection.singleFile

 

<< Groovy >>

```groovy
// Iterate over the files in the collection
collection.each { File file ->
    println file.name
}

// Convert the collection to various types
Set set = collection.files
Set set2 = collection as Set
List list = collection as List
String path = collection.asPath
File file = collection.singleFile

// Add and subtract collections
def union = collection + layout.files('src/file2.txt')
def difference = collection - layout.files('src/file2.txt')
```

------

\# Ex 4

파일 콜렉션을 필터링 하는 방법입니다.

파일 이름이 .txt 로 끝나는 파일만 걸러냅니다.

 

<< Groovy >>

```groovy
FileCollection textFiles = collection.filter { File f ->
    f.name.endsWith(".txt")
}
```

 

<< Command >>

```
> gradle -q filterTextFiles
src/file1.txt
src/file2.txt
src/file5.txt
```

 

 

## ◆ Gradle Build Script - 파일 트리

파일 트리는 파일 콜렉션과 달리 폴더의 계층구조까지 복사를 합니다.



![img](https://blog.kakaocdn.net/dn/lx2Sn/btqCqNCY4qj/x4GVLMaa7M8uWJeYCXU74K/img.png)

![img](https://blog.kakaocdn.net/dn/JNN4P/btqCqMxkFdT/BDdF05qQOuFfw7xKrQpwwk/img.png)



------

\# Ex 1

파일트리 생성하기

include / exclude 속성을 통해서 특정 패턴의 파일을 포함시키거나 제외시킬 수 있습니다.

 

<< Groovy >>

```groovy
// Create a file tree with a base directory
ConfigurableFileTree tree = fileTree(dir: 'src/main')

// Add include and exclude patterns to the tree
tree.include '**/*.java'
tree.exclude '**/Abstract*'

// Create a tree using closure
tree = fileTree('src') {
    include '**/*.java'
}

// Create a tree using a map
tree = fileTree(dir: 'src', include: '**/*.java')
tree = fileTree(dir: 'src', includes: ['**/*.java', '**/*.xml'])
tree = fileTree(dir: 'src', include: '**/*.java', exclude: '**/*test*/**')
```

------

\# Ex 2

Ant 의 기본설정되어있는 exclude 패턴을 제거하여 초기화 하는 방법입니다.

 

<< Groovy >>

```groovy
task forcedCopy (type: Copy) {
    into "$buildDir/inPlaceApp"
    from 'src/main/webapp'

    doFirst {
        ant.defaultexcludes remove: "**/.git"
        ant.defaultexcludes remove: "**/.git/**"
        ant.defaultexcludes remove: "**/*~"
    }

    doLast {
        ant.defaultexcludes default: true
    }
}
```

------

\# Ex 3

파일 트리를 사용하는 방법입니다.

\- 반복문 each

\- 필터링 matching > include

\- 더하기 +

\- 트리의 요소를 불러오기 visit

 

<< Groovy >>

```groovy
// Iterate over the contents of a tree
tree.each {File file ->
    println file
}

// Filter a tree
FileTree filtered = tree.matching {
    include 'org/gradle/api/**'
}

// Add trees together
FileTree sum = tree + fileTree(dir: 'src/test')

// Visit the elements of the tree
tree.visit {element ->
    println "$element.relativePath => $element.file"
}
```

 

 

## ◆ Gradle Build Script - 컴파일

\# Ex 1

type: JavaCompile 형태로 작업을 선언하면, 컴파일을 할 폴더를 설정할 수 있습니다.

아래의 스크립트는 source 라는 기본 속성에 컴파일을 할 파일경로를 넣는 방법입니다.

 

<< Groovy >>

```groovy
task compile(type: JavaCompile) {

    // Use a File object to specify the source directory
    source = file('src/main/java')

    // Use a String path to specify the source directory
    source = 'src/main/java'

    // Use a collection to specify multiple source directories
    source = ['src/main/java', '../shared/java']

    // Use a FileCollection (or FileTree in this case) to specify the source files
    source = fileTree(dir: 'src/main/java').matching { include 'org/gradle/api/**' }

    // Using a closure to specify the source files.
    source = {
        // Use the contents of each zip file in the src dir
        file('src').listFiles().findAll {it.name.endsWith('.zip')}.collect { zipTree(it) }
    }
}
```

------

\# Ex 2

아래와 같이 작업을 생성한 이후에 컴파일 경로를 추가할 수도 있습니다.

 

<< Groovy >>

```groovy
compile {
    // Add some source directories use String paths
    source 'src/main/java', 'src/main/groovy'

    // Add a source directory using a File object
    source file('../shared/java')

    // Add some source directories using a closure
    source { file('src/test/').listFiles() }
}
```

 

 

## ◆ Gradle Build Script - 좀 더 복잡한 필터링

\# Ex 1

토큰을 대체, 텍스트의 줄을 제거, - 로 시작하는 라인은 제거, 캐릭터셋 UTF-8 과 같이 여러가지의 작업을 필터링으로 가능합니다.

좀 더 복잡한 필터링의 경우에는 FixCrLfFilter 와 같이 템플릿으로 제공되어있기도 합니다.

 

<< Groovy >>

```groovy
import org.apache.tools.ant.filters.FixCrLfFilter
import org.apache.tools.ant.filters.ReplaceTokens

task filter(type: Copy) {
    from 'src/main/webapp'
    into "$buildDir/explodedWar"
    
    // Substitute property tokens in files
    expand(copyright: '2009', version: '2.3.1')
    expand(project.properties)
    
    // Use some of the filters provided by Ant
    filter(FixCrLfFilter)
    filter(ReplaceTokens, tokens: [copyright: '2009', version: '2.3.1'])
    
    // Use a closure to filter each line
    filter { String line ->
        "[$line]"
    }
    
    // Use a closure to remove lines
    filter { String line ->
        line.startsWith('-') ? null : line
    }
    
    filteringCharset = 'UTF-8'
}
```

 

 

## ◆ Gradle Build Script - CopySpec 을 사용한 파일 복사

CopySpec은 작업과 독립적이면서 계층구조를 가지고 있습니다.

------

\# Ex 1

'src/main/webapp' 폴더에 있는 html, png, jpg 파일들의 이름에서 "-staging" 문자열을 제거하도록 복사조건을 만듭니다.

복사 조건을 이용하여 복사 및 배포에 대한 조건을 템플릿처럼 사용할 수 있습니다.

\- 복사 : copyAssets

\- 배포 : distApp

 

<< Groovy >>

```groovy
CopySpec webAssetsSpec = copySpec {
    from 'src/main/webapp'
    include '**/*.html', '**/*.png', '**/*.jpg'
    rename '(.+)-staging(.+)', '$1$2'
}

task copyAssets (type: Copy) {
    into "$buildDir/inPlaceApp"
    with webAssetsSpec
}

task distApp(type: Zip) {
    archiveFileName = 'my-app-dist.zip'
    destinationDirectory = file("$buildDir/dists")

    from appClasses
    with webAssetsSpec
}
```

------

\# Ex 2

복사 패턴만 변수로 지정하여 공유할 수도 있습니다.

 

<< Groovy >>

```groovy
def webAssetPatterns = {
    include '**/*.html', '**/*.png', '**/*.jpg'
}

task copyAppAssets(type: Copy) {
    into "$buildDir/inPlaceApp"
    from 'src/main/webapp', webAssetPatterns
}

task archiveDistAssets(type: Zip) {
    archiveFileName = 'distribution-assets.zip'
    destinationDirectory = file("$buildDir/dists")

    from 'distResources', webAssetPatterns
}
```

 

 

## ◆ Gradle Build Script - 여러가지 조건의 복사



![img](https://blog.kakaocdn.net/dn/nXmCZ/btqCjRmSvTZ/2BvFJoum8W0lhHrKSbsR80/img.png)



------

\# Ex 1

위의 그림과 같이 폴더를 별도로 지정해서 빌드를 할 수 있습니다.

기본이 되는 경로는 into를 사용하여 초기에 지정합니다.

별도로 다른 경로에 복사하기를 원한다면, into 안에 from 을 넣어서 지정할 수도 있고, from 안에 into를 넣어 지정할 수도 있습니다. 하지만, 둘중 하나만 사용하여 일관성을 지키도록 합니다.

 

<< Groovy >>

```groovy
task nestedSpecs(type: Copy) {
    into "$buildDir/explodedWar"
    exclude '**/*staging*'
    from('src/dist') {
        include '**/*.html', '**/*.png', '**/*.jpg'
    }
    from(sourceSets.main.output) {
        into 'WEB-INF/classes'
    }
    into('WEB-INF/lib') {
        from configurations.runtimeClasspath
    }
}
```

------

\# Ex 2

복사 전용 작업이 아니라 (type: Copy 없이) 작업 내에서 복사를 실행합니다.

 

<< Groovy >>

```groovy
task copyMethod {
    doLast {
        copy {
            from 'src/main/webapp'
            into "$buildDir/explodedWar"
            include '**/*.html'
            include '**/*.jsp'
        }
    }
}
```

------

\# Ex 3

위의 작업을 인수로 받아서 사용하려면 input 과 output 을 명시적으로 작성해 주어야 합니다.

따라서 위의 방법을 사용하는것이 훨씬 좋습니다.

 

<< Groovy >>

```groovy
task copyMethodWithExplicitDependencies {

    // up-to-date check for inputs, plus add copyTask as dependency
    inputs.files(copyTask)
        .withPropertyName("inputs")
        .withPathSensitivity(PathSensitivity.RELATIVE)
        
    outputs.dir('some-dir') // up-to-date check for outputs
        .withPropertyName("outputDir")
        
    doLast{
        copy {
            // Copy the output of copyTask
            from copyTask
            into 'some-dir'
        }
    }
}
```

------

\# Ex 4

동기화된 작업으로 처리하는 방법입니다. 기본적으로는 파일에 대한 작업은 비동기로 처리를 하고 있습니다. 하지만 이 블록을 사용하면 삭제 전에 백업을 한다던지, 설치 후에 설치파일을 제거한다던지, 하는 작업들이 가능합니다.

type: Sync 형태로 작업을 생성하면 됩니다.

 

<< Groovy >>

```groovy
task libs(type: Sync) {
    from configurations.runtime
    into "$buildDir/libs"
}
```

 

 

## ◆ Gradle Build Script - 압축 후 이름 변경하기

\# Ex 1

별도로 설정하지 않으면 프로젝트 이름으로 압축파일이 생성됩니다.

 

<< Groovy >>

```groovy
plugins {
    id 'base'
}

version = 1.0

task myZip(type: Zip) {
    from 'somedir'

    doLast {
        println archiveFileName.get()
        println relativePath(destinationDirectory)
        println relativePath(archiveFile)
    }
}
```

 

<< Command >>

```
> gradle -q myZip
zipProject-1.0.zip
build/distributions
build/distributions/zipProject-1.0.zip
```

------

\# Ex 2

archiveBaseName 으로 직접 압축파일명을 지정할 수도 있습니다.

 

<< Groovy >>

```groovy
task myCustomZip(type: Zip) {
    archiveBaseName = 'customName'
    from 'somedir'

    doLast {
        println archiveFileName.get()
    }
}
```

 

<< Command >>

```
> gradle -q myCustomZip
customName-1.0.zip
```

------

\# Ex 3

압축파일의 이름에 수식어(version, appendix, classifier)를 넣어줄 수도 있습니다.

 

<< Groovy >>

```groovy
plugins {
    id 'base'
}

version = 1.0
archivesBaseName = "gradle"

task myZip(type: Zip) {
    from 'somedir'
}

task myOtherZip(type: Zip) {
    archiveAppendix = 'wrapper'
    archiveClassifier = 'src'
    from 'somedir'
}

task echoNames {
    doLast {
        println "Project name: ${project.name}"
        println myZip.archiveFileName.get()
        println myOtherZip.archiveFileName.get()
    }
}
```

 

<< Command >>

```
> gradle -q echoNames
Project name: zipProject
gradle-1.0.zip
gradle-wrapper-1.0-src.zip
```

------

\# Ex 4

동일한 빌드의 결과물을 재생산 할 수 있도록 timestamp 를 기록하지 않도록 하고, 파일의 순서를 지정하도록 설정할 수 있습니다.

 

<< Groovy >>

```groovy
tasks.withType(AbstractArchiveTask) {
    preserveFileTimestamps = false
    reproducibleFileOrder = true
}
```