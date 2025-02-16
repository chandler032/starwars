import {Box, styled, Typography} from "@mui/material";
import ResultTitle from "./ResultTitle.jsx";
import AppearanceStat from "./appearance/AppearanceStat.jsx";
import {useSelector} from "react-redux";
import {getSearchText, getSearchType, resultCount, searchResult} from "../../../store/searchSlice.js";
import {useEffect, useState} from "react";
import ContentSkeleton from "./loaders/ContentSkeleton.jsx";
import FilmTable from "./results/FilmTable.jsx";
import OtherTable from "./results/OtherTable.jsx";

const ContentContainer = styled(Box)(({theme}) => ({
  flex: "0 0 75%",
  backgroundColor: theme.palette.grey[50],
  padding: theme.spacing(2),
  [theme.breakpoints.down("md")]: {
    flex: "none",
    width: "100%",
    height: "100vh"
  }
}))

const ContentBox = styled(Box)(({theme}) => ({
  display: "flex",
  flexDirection: "column",
  width: "100%",
  backgroundColor: theme.palette.grey[100],
  color: theme.palette.grey[800],
  padding: theme.spacing(2),
}))

const AppearanceContainer = styled(Box)(({theme}) => ({
  display: "flex",
  flexDirection: "row",
  flexWrap: "wrap",
  paddingTop: theme.spacing(2),
}))

const Content = () => {
  const searchApiResult = useSelector(searchResult);
  const searchResultCount = useSelector(resultCount);
  const searchText = useSelector(getSearchText);
  const searchType = useSelector(getSearchType);
  const [showResultContent, setShowResultContent] = useState(false);

  useEffect(() => {
    if (searchResultCount && searchResultCount > 0) {
      setShowResultContent(true);
    } else {
      setShowResultContent(false);
    }
  }, [searchResultCount]);

  return (
    <ContentContainer>
      <ContentBox>
        {showResultContent ?
          <>
            <ResultTitle title={searchText}/>
            <Typography sx={{paddingTop: "16px"}} variant="body1">Total {searchType} found: {searchResultCount}</Typography>
            {searchType === "film" ? <FilmTable data={searchApiResult}/> : <OtherTable data={searchApiResult}/>}
          </>
          : <ContentSkeleton/>}
      </ContentBox>
    </ContentContainer>
  )
}

export default Content;