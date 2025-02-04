import http from 'k6/http';
import { sleep } from 'k6';
import { removeBackground } from "./remove-background.js";
import { upscale } from "./upscale.js";
import { textToImage } from "./text-to-image.js";
import { eraseArea } from "./erase-area.js";
import { imageToImage } from "./image-to-image.js";
import { outpaint } from "./outpaint.js";
import { recolor } from "./recolor.js";
import { recommendedImage } from "./recommended-image.js";
import { replaceBackground } from "./replace-background.js";

/**
 * {@link https://grafana.com/docs/k6/latest/javascript-api/k6-http/set-response-callback/#example}
 */
http.setResponseCallback(http.expectedStatuses({ min: 200, max: 399 }));

const testFunctions = {
    "erase-area": eraseArea,
    "image-to-image": imageToImage,
    "outpaint": outpaint,
    "recolor": recolor,
    "recommended-image": recommendedImage,
    "remove-background": removeBackground,
    "replace-background": replaceBackground,
    "text-to-image": textToImage,
    "upscale": upscale,
};

const externalApiTests = [
    'erase-area',
    'recommended-image',
    'replace-background',
    'remove-background',
];

const enableExternalApiTests = __ENV.K6_ENABLE_EXTERNAL_API === 'true'

let enabledTests = __ENV.K6_TEST ? __ENV.K6_TEST.split(',').map(test => test.trim()) : [];

export function setup() {
    console.log('받은 옵션들:');
    console.log(`K6_TEST: ${__ENV.K6_TEST}`);
    console.log(`K6_DURATION: ${__ENV.K6_DURATION}`);
    console.log(`K6_VUS: ${__ENV.K6_VUS}`);
    console.log(`K6_URL: ${__ENV.K6_URL}`);
    console.log(`K6_SESSION_ID: ${__ENV.K6_SESSION_ID}`);
    console.log(`K6_REQUIRED_POLLING: ${__ENV.K6_REQUIRED_POLLING}`);
    console.log(`K6_SAME_AUTH: ${__ENV.K6_SAME_AUTH}`);
    console.log(`입력받은 테스트: ${enabledTests.join(', ')}`);

    const testsNeedingConfirmation = enabledTests.filter((test) =>
        externalApiTests.includes(test)
    );

    if (testsNeedingConfirmation.length > 0 && !enableExternalApiTests) {
        console.log(
            `다음 테스트는 실행 전에 확인이 필요합니다: ${testsNeedingConfirmation.join(', ')}`
        );
        console.log('K6_ENABLE_EXTERNAL_API 환경 변수가 설정되지 않아 테스트를 건너뜁니다.');

        // 확인이 필요한 테스트를 enabledTests에서 제거
        enabledTests = enabledTests.filter(
            (test) => !testsNeedingConfirmation.includes(test)
        );
    } else if (enableExternalApiTests) {
        console.log('K6_ENABLE_EXTERNAL_API=true 이므로 테스트를 진행합니다.');
    }

    console.log(`실제 실행되는 테스트: ${enabledTests.join(', ')}`);

    return enabledTests;
}

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]]; // swap
    }
    return array;
}

/**
 *
 * @param filteredTests setup 함수에서 리턴한 실제 실행할 테스트
 */
export default function (filteredTests) {
    const shuffledTests = shuffleArray([...filteredTests]);

    shuffledTests.forEach(testName => {
        const testFunction = testFunctions[testName];
        if (testFunction) {
            console.log(`실행되는 테스트: ${testName}`);
            testFunction();  // 맵에서 해당 함수 호출
        }
    });
    sleep(2); // 시나리오 하나 끝나면 2초 대기
}