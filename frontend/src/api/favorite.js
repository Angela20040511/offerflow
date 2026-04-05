import request from './request'
export const addFavoriteApi = (data) => request.post('/favorites', data)
export const removeFavoriteApi = (jobId) => request.delete(`/favorites/${jobId}`)
export const getMyFavoritesApi = (params) => request.get('/favorites/me', { params })
