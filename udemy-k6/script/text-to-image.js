import {sendRequest, pollingMultipleTaskRequests, updateIterations, getAuthValue} from "./base.js";


const type = 'text-to-image';


const requiredPolling = __ENV.K6_REQUIRED_POLLING === 'true';  // 환경변수에서 required_polling 값을 받아옴

export function textToImage() {
    const uuid = sendRequest(type, () => {
        const jsonBody = JSON.stringify({
            prompt1: "팬더",
            presetKey: "animation",
            auth: `${getAuthValue()}`,
        });

        return {
            body: jsonBody,
            headers: { 'Content-Type': 'application/json' },
        }
    });

    if (requiredPolling && uuid) {
        pollingMultipleTaskRequests(type, uuid);
    }

    updateIterations(type);
}
