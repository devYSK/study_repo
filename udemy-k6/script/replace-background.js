import { FormData } from 'https://jslib.k6.io/formdata/0.0.2/index.js';
import {getAuthValue, pollingMultipleTaskRequests, sendRequest, updateIterations} from "./base.js";

const requiredPolling = __ENV.K6_REQUIRED_POLLING === 'true';  // 환경변수에서 required_polling 값을 받아옴

const type = 'replace-background';

export function replaceBackground() {
    const uuid = sendRequest(type, () => {
        const formData = new FormData();
        formData.append('auth', `${getAuthValue()}`);
        formData.append('imageUrl', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdeN-hkCRGNc1dtOWpdc1gPHGZaPCjrP3VYA&s');
        formData.append('prompt', 'A beautiful landscape with a clear sky and a mountain in the background.');

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
