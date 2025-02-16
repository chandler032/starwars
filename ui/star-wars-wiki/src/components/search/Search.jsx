import {Button, FormControl, InputLabel, MenuItem, Select, TextField} from "@mui/material";
import {useState} from "react";
import {useDispatch} from "react-redux";
import {resetResultCount, searchData, setSearchQuery, setSearchQueryType} from "../../store/searchSlice.js";

const Search = () => {
  const [selectedType, setSelectedType] = useState('');
  const [searchText, setSearchText] = useState('');
  const dispatch = useDispatch();

  const handleTypeChange = (event) => {
    console.log("selected type", event.target.value);
    setSelectedType(event.target.value);
  }

  const handleSearchTextChange = (event) => {
    console.log("search text", event.target.value);
    setSearchText(event.target.value);
  }

  const handleSearchClick = () => {
    dispatch(resetResultCount(0));
    let searchQuery = {
      type: selectedType,
      value: searchText
    }

    dispatch(setSearchQuery(searchText));
    dispatch(setSearchQueryType(selectedType));
    dispatch(searchData(searchQuery));

  }

  return (
    <>
      <FormControl sx={{width: {xs: "34%", md: "100%"}}} required fullWidth size="small">
        <InputLabel id="type-select-label>">Select Type</InputLabel>
        <Select
          labelId="type-select-label"
          id="type-select"
          label="Select Type"
          variant="outlined" value={selectedType} onChange={handleTypeChange}>
          <MenuItem value="people">People</MenuItem>
          <MenuItem value="film">Films</MenuItem>
          <MenuItem value="starship">Starships</MenuItem>
          <MenuItem value="vehicle">Vehicles</MenuItem>
          <MenuItem value="species">Species</MenuItem>
          <MenuItem value="planet">Planets</MenuItem>
        </Select>
      </FormControl>
      <TextField sx={{marginTop: {md: "16px"}, width: {xs: "33%", md: "100%"}}}
                 required
                 variant="outlined"
                 label="Search For"
                 id="search-text"
                 size="small"
                 onChange={handleSearchTextChange}
      />

      <Button size="medium"
              sx={{width: {xs: "33%", md: "100%"}, marginTop: {md: "16px"}}}
              variant="outlined"
              color="success" onClick={handleSearchClick}>
        Search
      </Button>
    </>
  )
}

export default Search;