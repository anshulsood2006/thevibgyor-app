import React, { useState, useEffect } from 'react';
import './DictionaryConfiguration.css';

const DictionaryConfiguration = () => {
    const [selectedLetter, setSelectedLetter] = useState('A');
    const [words, setWords] = useState([]);
    const [languages, setLanguages] = useState([]);
    const [isPopupVisible, setIsPopupVisible] = useState(false);
    const [newWord, setNewWord] = useState({ word: '', languageInputs: [{ language: 'HINGLISH', input: '' }] });

    const fetchWords = async (letter) => {
        try {
            const token = localStorage.getItem("token");
            const response = await fetch(`${process.env.BACKEND_API_BASE_URL}${process.env.SUPER_ADMIN}${process.env.DICTIONARY}${process.env.WORDS}?letter=${encodeURIComponent(letter)}`, {
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
            setWords(data.response.data);
        } catch (error) {
            console.log(error.message);
        }
    };

    useEffect(() => {
        fetchWords(selectedLetter);
    }, [selectedLetter]);

    const handleLetterClick = (letter) => {
        setSelectedLetter(letter);
    };

    const fetchLanguages = async () => {
        try {
            const token = localStorage.getItem("token");
            const response = await fetch(`${process.env.BACKEND_API_BASE_URL}${process.env.SUPER_ADMIN}${process.env.DICTIONARY}${process.env.LANGUAGES}`, {
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
            setLanguages(data.response.data);
        } catch (error) {
            console.log(error.message);
        }
    };

    const handleAddWordClick = () => {
        setIsPopupVisible(true);
        fetchLanguages();
    };

    const handleInputChange = (e, index) => {
        const { name, value } = e.target;
        const updatedLanguageInputs = newWord.languageInputs.map((input, i) =>
            i === index ? { ...input, [name]: value } : input
        );
        setNewWord({ ...newWord, languageInputs: updatedLanguageInputs });
    };

    const handleAddLanguageInput = () => {
        setNewWord({
            ...newWord,
            languageInputs: [...newWord.languageInputs, { language: 'HINGLISH', input: '' }]
        });
    };

    const handleRemoveLanguageInput = (index) => {
        setNewWord({
            ...newWord,
            languageInputs: newWord.languageInputs.filter((_, i) => i !== index)
        });
    };

    const handleSubmit = async () => {
        const requestBody = {
            input: {
                language: 'ENGLISH',
                text: newWord.word,
            },
            meanings: newWord.languageInputs.map(input => ({
                language: input.language,
                meaning: input.input
            }))
        };

        try {
            const token = localStorage.getItem("token");
            const response = await fetch(`${process.env.BACKEND_API_BASE_URL}${process.env.SUPER_ADMIN}${process.env.DICTIONARY}${process.env.WORDS}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(requestBody)
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const firstLetter = newWord.word.charAt(0).toUpperCase();
            setSelectedLetter(firstLetter);
            fetchWords(selectedLetter);
            setWords(prevWords => [{ input: requestBody.input, meanings: requestBody.meanings }, ...prevWords]);
            handlePopupClose();
        } catch (error) {
            console.log(error.message);
        }
    };

    const handlePopupClose = () => {
        setIsPopupVisible(false);
        setNewWord({ word: '', languageInputs: [] }); // Reset state when closing the popup
    };

    const renderWordsTable = () => {
        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Word</th>
                            <th>Meanings</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                        words.length === 0 ?
                        (
                            <tr>
                                <td colSpan="2" className="no-words">No word found in dictionary for '{selectedLetter}'</td>
                            </tr>
                        ) :
                        words.map(
                        (wordEntry, index) =>
                                (
                                    <tr key={index}>
                                        <td>{wordEntry.input.text}</td>
                                        <td>
                                            {wordEntry.meanings.map((meaning, mIndex) => (
                                                <div key={mIndex}>{meaning.meaning}</div>
                                            ))}
                                        </td>
                                    </tr>
                                )
                        )
                        }
                    </tbody>
                </table>
            </div>
        );
    };

    const renderPopup = () => {
        if (!isPopupVisible) return null;
        return (
            <div className="popup-overlay">
                <div className="popup-content">
                    <h3>Add New Word</h3>
                    <div className="form-group">
                        <label>Word:</label>
                        <input
                            type="text"
                            name="word"
                            value={newWord.word}
                            onChange={(e) => setNewWord({ ...newWord, word: e.target.value })}
                        />
                    </div>
                    <div className="form-group language-inputs">
                        <div className="add-meaning-container">
                            <label>Meaning:</label>
                            <button className="add-button" onClick={handleAddLanguageInput}>
                                Add Meaning
                            </button>
                        </div>
                        {newWord.languageInputs.map((input, index) => (
                            <div key={index} className="language-input-group">
                                <select
                                    name="language"
                                    value={input.language}
                                    onChange={(e) => handleInputChange(e, index)}
                                >
                                    <option value="">Language</option>
                                    {languages.map((lang, i) => (
                                        <option key={i} value={lang}>
                                            {lang}
                                        </option>
                                    ))}
                                </select>
                                <input
                                    type="text"
                                    name="input"
                                    value={input.input}
                                    onChange={(e) => handleInputChange(e, index)}
                                />
                                <button
                                    className="remove-button"
                                    onClick={() => handleRemoveLanguageInput(index)}
                                >
                                    &ndash;
                                </button>
                            </div>
                        ))}
                    </div>
                    <button onClick={handleSubmit}>Submit</button>
                    <button onClick={handlePopupClose}>Close</button>
                </div>
            </div>
        );
    };

    return (
        <div className="dictionary-container">
            <div className="dictionary-header">Select an alphabet to search corresponding word</div>
            <div className="alphabet-header">
                {'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('').map((letter) => (
                    <div
                        key={letter}
                        className={`circle ${selectedLetter === letter ? 'selected' : ''}`}
                        onClick={() => handleLetterClick(letter)}
                    >
                        {letter}
                    </div>
                ))}
            </div>
            <button className="add-word-button" onClick={handleAddWordClick}>Add New Word</button>
            <div className="words-list">
                <ul>{renderWordsTable()}</ul>
            </div>
            {renderPopup()}
        </div>
    );
};

export default DictionaryConfiguration;