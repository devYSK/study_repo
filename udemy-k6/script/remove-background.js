import { FormData } from 'https://jslib.k6.io/formdata/0.0.2/index.js';
import {sendRequest, updateIterations, pollingSingleTaskRequests, getAuthValue} from './base.js';

const type = 'remove-background';

const requiredPolling = __ENV.K6_REQUIRED_POLLING === 'true';  // 환경변수에서 required_polling 값을 받아옴

export function removeBackground () {
    const uuid = sendRequest('remove-background', () => {
        const formData = new FormData();
        formData.append('auth', `${getAuthValue()}`);
        formData.append('originalImageUrl', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdeN-hkCRGNc1dtOWpdc1gPHGZaPCjrP3VYA&s');

        return {
            body: formData.body(),
            headers: { 'Content-Type': `multipart/form-data; boundary=${formData.boundary}` },
        };
    });

    if (requiredPolling && uuid) {
        pollingSingleTaskRequests(type, uuid);
    }

    updateIterations(type);
}
