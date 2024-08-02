##  Example to call GET api and print response on UI

```
import React, { useState, useEffect} from 'react';
const Example = () => {
const [apiResponse, setApiResponse] = useState(null);

    useEffect(() => {
        const fetchWords = async () => {
            try {
                //You should have token already set in localStorage if the API is secured via Bearer token
                const token = localStorage.getItem("token");
                const response = await fetch(`http://localhost:8080/sa/dictionary/words?letter=${encodeURIComponent('A')}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setApiResponse(data);
            } catch (error) {
                console.log(error);
            } finally {
                console.log('ok')
            }
        };
        fetchWords();
    }, []);

    const renderResponse = () => {
        return(
            <div>
                {apiResponse && (
                    <pre>{JSON.stringify(apiResponse, null, 2)}</pre>
                )}
            </div>);
    };

    return (
            <div>
                <ul>{renderResponse()}</ul>
            </div>
    );
};

export default Example;
```