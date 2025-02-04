import { FormData } from 'https://jslib.k6.io/formdata/0.0.2/index.js';
import {getAuthValue, pollingMultipleTaskRequests, sendRequest, updateIterations} from "./base.js";


// 사용자 정의 메트릭 설정
const type = 'image-to-image';

const requiredPolling = __ENV.K6_REQUIRED_POLLING === 'true';  // 환경변수에서 required_polling 값을 받아옴

export function imageToImage() {
    const uuid = sendRequest(type, () => {
        const formData = new FormData();
        formData.append('auth', `${getAuthValue()}`);
        formData.append('imageUrl', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdeN-hkCRGNc1dtOWpdc1gPHGZaPCjrP3VYA&s');
        formData.append('prompt1', '팬더');
        formData.append('presetKey', 'animation');

        return {
            body: formData.body(),
            headers: { 'Content-Type': `multipart/form-data; boundary=${formData.boundary}` },
        };
    });

    if (requiredPolling && uuid) {
        pollingMultipleTaskRequests(type, uuid);
    }

    updateIterations(type);
}
