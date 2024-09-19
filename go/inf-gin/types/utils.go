package types

type ApiResponse struct {
	Result      int64       `json:"result"`
	Description string      `json:"description"`
	ErrCode     interface{} `json:"errCode"`
}

func NewApiResponse(result int64, description string, errCode interface{}) *ApiResponse {
	return &ApiResponse{
		Result:      result,
		Description: description,
		ErrCode:     errCode,
	}
}
