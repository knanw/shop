import {defineStore} from 'pinia'

export function createStore(id: string, endpoint: string) {
    return defineStore(id, () => {
            const items = ref([])
            const price = ref('')

            const gets = computed(() => {
                return items.value
            })

            async function init() {
                await fetchItems()
            }

            async function fetchItems() {
                items.value = []
                items.value = await fetch(`/api/v1/${endpoint}`)
                    .then(async (response) => await response.json())
                    .catch((e) => {
                        console.error('Failed to fetch:', e)
                    })
            }

            async function add(item: Record<string, any>) {
                const requestInit: RequestInit = {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(item)
                };

                try {
                    const response = await fetch(`/api/v1/${endpoint}`, requestInit);
                    if (!response.ok) {
                        throw new Error(`Fehler bei der Anfrage: ${response.statusText}`);
                    }
                    const result = await response.json();
                } catch (error) {
                    console.error('Failed to post:', error);
                }
            }

        async function change(item: Record<string, any>) {
            const requestInit: RequestInit = {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(item)
            }
            try {
                const response = await fetch(`/api/v1/${endpoint}/${item.id}` , requestInit);

                if (!response.ok) {
                    throw new Error(`Fehler bei der Anfrage: ${response.statusText}`);
                }
                const result = await response.json();
            } catch (error) {
                console.error('Failed to put:', error);
            } finally {
                fetchItems()
            }
        }

            async function remove(id: any) {
                await fetch(`/api/v1/${endpoint}/${id}`, {method: 'DELETE'})
                    .then((result: any) => {
                        fetchItems()
                    })
                    .catch((e) => {
                        console.error('Failed to remove:', e)
                    })
            }

            async function calculatePrice(entries: BooksDTO []) {
                const requestInit = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(entries)
                }

                try {
                    const response = await fetch('api/v1/shop/calculate', requestInit);
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    console.log('Response:', response);
                    price.value = await response.text();
                } catch (e) {
                    console.error('Failed to put:', e);
                }
            }

            return {
                init,
                gets,
                add,
                change,
                remove,
                calculatePrice,
                price,
            }
        }
    )()
}

export function useBookStore() {
    return createStore('book', 'book');
}

export function useSeriesStore() {
    return createStore('series', 'series');
}